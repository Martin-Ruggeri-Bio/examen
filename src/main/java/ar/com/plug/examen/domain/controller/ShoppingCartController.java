package ar.com.plug.examen.domain.controller;

import ar.com.plug.examen.domain.dtos.Products;
import ar.com.plug.examen.domain.dtos.Message;
import ar.com.plug.examen.domain.model.ShoppingCart;
import ar.com.plug.examen.domain.model.User;
import ar.com.plug.examen.domain.service.ShoppingCartService;
import ar.com.plug.examen.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shoppingList")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
	private UserService userService;

    @GetMapping()
    public ResponseEntity<List<ShoppingCart>> getListByClient(@RequestHeader("Authorization") String tokenHeader){
        String token = tokenHeader.replace("Bearer ", "");
        Optional<User> userOptional = userService.getByToken(token);
        User user = userOptional.orElse(null);
        
        if (user == null) {
            log.error("Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String clientId = user.getId();
        log.info("Lista de compras obtenida con éxito");
        return new ResponseEntity<>(this.shoppingCartService.getListByClient(clientId), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countByClient(@RequestHeader("Authorization") String tokenHeader){
        String token = tokenHeader.replace("Bearer ", "");
        Optional<User> userOptional = userService.getByToken(token);
        User user = userOptional.orElse(null);
        
        if (user == null) {
            log.error("Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!user.getRole().equals("client")) {
            log.error("No tiene permisos");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String clientId = user.getId();
        log.info("Cantidad de productos en carrito obtenida con éxito");
        return new ResponseEntity<>(this.shoppingCartService.getCountByClient(clientId),HttpStatus.OK);
    }

    @PostMapping("/addProduct")
    public ResponseEntity<Message> addProduct(@RequestHeader("Authorization") String tokenHeader,
        @Valid @RequestBody Products products, BindingResult bindingResult){
            String token = tokenHeader.replace("Bearer ", "");
            if (token == null) {
                log.error("Token no encontrado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Optional<User> userOptional = userService.getByToken(token);
            User user = userOptional.orElse(null);
            if (user == null) {
                log.error("Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            //si el rol del usuario no es cliente 
            if (!user.getRole().equals("client")) {
                log.error("No tiene permisos");
                return new ResponseEntity<>(new Message("No tiene permisos"),HttpStatus.UNAUTHORIZED);
            }
            if (bindingResult.hasErrors()){
                log.error("Revise los campos");
                return new ResponseEntity<>(new Message("Revise los campos"),HttpStatus.BAD_REQUEST);
            }
            ShoppingCart shoppingCartOld = this.shoppingCartService.getByClientAndProduct(user.getId(), products.getProduct().getId());
            if (shoppingCartOld != null) {
                log.info("Actualizando Procucto en carrito");
                this.shoppingCartService.updateProduct(shoppingCartOld, products.getProduct(), products.getAmount(), user);
            } else {
                this.shoppingCartService.addProduct(products.getProduct(), products.getAmount(), user);
            }
            log.info("Producto agregado");
            return new ResponseEntity<>(new Message("Producto agregado"),HttpStatus.OK);
    }


    @DeleteMapping("/clean/{item_id}")
    public ResponseEntity<Message> removeProduct(@RequestHeader("Authorization") String tokenHeader, @PathVariable("item_id")String id){
        String token = tokenHeader.replace("Bearer ", "");
        if (token == null) {
            log.error("Token no encontrado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<User> userOptional = userService.getByToken(token);
        User user = userOptional.orElse(null);
        if (user == null) {
            log.error("Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!user.getRole().equals("client")) {
            log.error("No tiene permisos");
            return new ResponseEntity<>(new Message("No tiene permisos"),HttpStatus.UNAUTHORIZED);
        }
        Long idLong = Long.parseLong(id);
        ShoppingCart shoppingCartOld = this.shoppingCartService.getByClientAndProduct(user.getId(), idLong);
        if (shoppingCartOld == null) {
            log.error("Producto no encontrado");
            return new ResponseEntity<>(new Message("Producto no encontrado"),HttpStatus.NOT_FOUND);
        }
        this.shoppingCartService.delete(shoppingCartOld.getId());
        log.info("Producto eliminado");
        return new ResponseEntity<>(new Message("Eliminado"),HttpStatus.OK);
    }
}