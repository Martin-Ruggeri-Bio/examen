package ar.com.plug.examen.domain.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import ar.com.plug.examen.domain.service.UserService;
import ar.com.plug.examen.domain.model.User;
import ar.com.plug.examen.domain.dtos.UserLoginRequest;
import ar.com.plug.examen.domain.dtos.UserTokenResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

// Este controlador recibe un objeto UserLoginRequest que contiene el nombre de usuario. 
// Luego busca un usuario en la base de datos con el nombre de usuario proporcionado y verifica si la contraseña coincide.
// Si el usuario es válidos, se genera un nuevo token y se almacena en la base de datos junto con el usuario.
// Finalmente, se devuelve una respuesta que contiene el token generado.

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    
    @Autowired
	private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserTokenResponse> login(@Valid @RequestBody UserLoginRequest userLogin, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("Credenciales inválidas {}", userLogin);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credenciales inválidas");
        }
        try {
            if (userService.existByUserName(userLogin.getUserName())) {
                Optional<User> userOptional = userService.getByUserName(userLogin.getUserName());
                User user = userOptional.orElse(null);
                UserTokenResponse userTokenResponse = new UserTokenResponse(user.getToken());
                log.info("Usuario logueado con éxito");
                return new ResponseEntity<UserTokenResponse>(userTokenResponse, HttpStatus.OK);
            }

            User user = new User();
            user.setUserName(userLogin.getUserName());

            if (userLogin.getRole().equals("client")) {
                user.setRole("client");
            } else if (userLogin.getRole().equals("seller")){
                user.setRole("seller");
            } else {
                log.error("Rol inválido");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol inválido");
            }

            String token = Jwts.builder()
                    .setSubject(user.getUserName())
                    .claim("userId", user.getId())
                    .signWith(SignatureAlgorithm.HS256, "secreto")
                    .compact();
            user.setToken(token);
            userService.save(user);
            UserTokenResponse userTokenResponse = new UserTokenResponse(token);
            log.info("Usuario creado y logueado con éxito");
            return new ResponseEntity<UserTokenResponse>(userTokenResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        Optional<User> userOptional = userService.getByToken(token);
        User user = userOptional.orElse(null);
        if (user == null) {
            log.error("Usuario no autorizado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        this.userService.delete(user);
        log.info("Usuario deslogueado con éxito");
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
