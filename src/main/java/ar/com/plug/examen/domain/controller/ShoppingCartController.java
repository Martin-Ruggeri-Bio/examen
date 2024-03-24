package ar.com.plug.examen.domain.controller;

import ar.com.plug.examen.domain.dtos.Products;
import ar.com.plug.examen.domain.dtos.Message;
import ar.com.plug.examen.domain.model.ShoppingCart;
import ar.com.plug.examen.domain.model.User;
import ar.com.plug.examen.domain.service.ShoppingCartService;
import ar.com.plug.examen.domain.service.UserService;

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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String clientId = user.getId();
        return new ResponseEntity<>(this.shoppingCartService.getListByClient(clientId), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countByClient(@RequestHeader("Authorization") String tokenHeader){
        String token = tokenHeader.replace("Bearer ", "");
        Optional<User> userOptional = userService.getByToken(token);
        User user = userOptional.orElse(null);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String clientId = user.getId();
        return new ResponseEntity<>(this.shoppingCartService.getCountByClient(clientId),HttpStatus.OK);
    }

    @PostMapping("/addProduct")
    public ResponseEntity<Message> addProduct(@RequestHeader("Authorization") String tokenHeader,
        @Valid @RequestBody Products products, BindingResult bindingResult){
            String token = tokenHeader.replace("Bearer ", "");
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Optional<User> userOptional = userService.getByToken(token);
            User user = userOptional.orElse(null);
            
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            if (bindingResult.hasErrors())
                return new ResponseEntity<>(new Message("Revise los campos"),HttpStatus.BAD_REQUEST);
            ShoppingCart shoppingCartOld = this.shoppingCartService.getByClientAndProduct(user.getId(), products.getProduct().getId());
            if (shoppingCartOld != null) {
                // si el product ya esta en carrito del cliente aumenta su cantidad
                ShoppingCart shoppingCartNew = new ShoppingCart();
                shoppingCartNew.setProduct(products.getProduct());
                shoppingCartNew.setClient(user);
                shoppingCartNew.setAmount(shoppingCartOld.getAmount() + products.getAmount());
                this.shoppingCartService.updateProduct(shoppingCartOld.getId(), shoppingCartNew);
            } else {
                //sino lo agrega
                ShoppingCart shoppingCart = new ShoppingCart();
                shoppingCart.setProduct(products.getProduct());
                shoppingCart.setClient(user);
                shoppingCart.setAmount(products.getAmount());
                this.shoppingCartService.addProduct(shoppingCart);
            }
            return new ResponseEntity<>(new Message("Producto agregado"),HttpStatus.OK);
    }


    @DeleteMapping("/clean/{item_id}")
    public ResponseEntity<Message> removeProduct(@PathVariable("item_id")String id){
        this.shoppingCartService.removeProduct(id);
        return new ResponseEntity<>(new Message("Eliminado"),HttpStatus.OK);
    }

    @PutMapping("/update/{item_id}")
    public ResponseEntity<Message> updateProduct(@PathVariable("item_id")String id, @RequestHeader("Authorization") String tokenHeader,
        @Valid @RequestBody Products products, BindingResult bindingResult){
            String token = tokenHeader.replace("Bearer ", "");
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Optional<User> userOptional = userService.getByToken(token);
            User user = userOptional.orElse(null);
            
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            if (bindingResult.hasErrors())
                return new ResponseEntity<>(new Message("Revise los campos"),HttpStatus.BAD_REQUEST);
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setProduct(products.getProduct());
            shoppingCart.setClient(user);
            shoppingCart.setAmount(products.getAmount());
            this.shoppingCartService.updateProduct(id, shoppingCart);
            return new ResponseEntity<>(new Message("Actualizado correctamente"),HttpStatus.OK);
    }
}
