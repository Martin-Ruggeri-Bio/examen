package ar.com.plug.examen.domain.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ar.com.plug.examen.domain.exception.ProductNotFoundException;
import ar.com.plug.examen.domain.model.Product;
import ar.com.plug.examen.domain.model.User;
import ar.com.plug.examen.domain.service.ProductService;
import ar.com.plug.examen.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author Martin
 *
 */
@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

	@Autowired
	private ProductService service;

	@Autowired
	private UserService userService;

	@GetMapping("/all")
	public ResponseEntity<List<Product>> findAll() {
		log.info("Buscando todos los productos");
		return new ResponseEntity<List<Product>>(service.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> findById(@PathVariable Long id) {
		try {
			log.info("Buscando producto por id {}", id);
			return new ResponseEntity<Product>(service.findById(id), HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			log.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/create")
	public ResponseEntity<Product> add(@RequestHeader(value="Authorization", required=true) String tokenHeader, @Valid @RequestBody Product product, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			log.error("Datos de producto incorrectos {}", product);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datos de producto incorrectos");
		}
		String token = tokenHeader.replace("Bearer ", "");
        if (token == null) {
            log.error("Usuario no autorizado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<User> userOptional = userService.getByToken(token);
        User user = userOptional.orElse(null);
        
        if (user == null) {
            log.error("Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
		if (!user.getRole().equals("seller")) {
            log.error("Rol no autorizado");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
		try {
			log.info("Creando producto {}", product);
			return new ResponseEntity<Product>(service.add(product), HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			log.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
		try{
			log.debug("Eliminando cliente {}", id);
			return new ResponseEntity<Boolean>(service.deleteById(id), HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			log.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Product> update(@RequestBody Product product, @PathVariable Long id) {
		try{
			log.info("Actualizando producto {}", product);
			return new ResponseEntity<Product>(service.update(product, id), HttpStatus.OK);
		} catch (ProductNotFoundException e){
			log.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}
