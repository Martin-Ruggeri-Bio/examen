package ar.com.plug.examen.domain.controller;

import ar.com.plug.examen.domain.dtos.DateRange;
import ar.com.plug.examen.domain.dtos.Message;
import ar.com.plug.examen.domain.dtos.Sales;
import ar.com.plug.examen.domain.model.Sale;
import ar.com.plug.examen.domain.model.User;
import ar.com.plug.examen.domain.service.SaleService;
import ar.com.plug.examen.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

@RestController
@RequestMapping("/sale")
@Slf4j
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
	private UserService userService;


    @GetMapping("/all")
    public ResponseEntity<List<Sale>> getAll(@RequestHeader(value="Authorization", required=true) String tokenHeader){
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
        log.info("Ventas obtenidas con éxito");
        return new ResponseEntity<>(this.saleService.getSalesAll(), HttpStatus.OK);
    }

    // SaleControler
    @PostMapping("/date-between")
    public ResponseEntity<Sales> findByDateBetween(@RequestHeader(value="Authorization", required=true) String tokenHeader,
                                                        @Valid @RequestBody DateRange dateRange, BindingResult bindingResult) {
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
        Sales sales = new Sales(this.saleService.findByDateBetween(dateRange.getFechaInicio(), dateRange.getFechaFin()));
        log.info("Ventas obtenidas con éxito");
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }


    // ventas realizadas a un cliente especifico
    @PostMapping("/client")
    public ResponseEntity<List<Sale>> getByClient(@RequestHeader(value="Authorization", required=true) String tokenHeader,
    @Valid @RequestBody User client, BindingResult bindingResult){
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

        String clientId = client.getId();
        log.info("Ventas obtenidas con éxito");
        return new ResponseEntity<>(this.saleService.getSalesByClient(clientId), HttpStatus.OK);
    }

    @GetMapping("/seller")
    public ResponseEntity<List<Sale>> getBySeller(@RequestHeader(value="Authorization", required=true) String tokenHeader){
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

        String sellerId = user.getId();
        log.info("Ventas obtenidas con éxito");
        return new ResponseEntity<>(this.saleService.getSalesBySeller(sellerId), HttpStatus.OK);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<Message> createSale(@RequestHeader(value="Authorization", required=true) String tokenHeader,
        @Valid @RequestBody User client, BindingResult bindingResult){
        String token = tokenHeader.replace("Bearer ", "");
        if (token == null) {
            log.error("Usuario no autorizado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<User> userOptional = userService.getByToken(token);
        User seller = userOptional.orElse(null);
        
        if (seller == null) {
            log.error("Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!seller.getRole().equals("seller")) {
            log.error("Rol no autorizado");
            return new ResponseEntity<>(new Message("No tiene permisos"),HttpStatus.UNAUTHORIZED);
        }
        String sellerId = seller.getId();
        if (!client.getRole().equals("client")) {
            log.error("El cliente no es valido");
            return new ResponseEntity<>(new Message("El cliente no es valido"),HttpStatus.UNAUTHORIZED);
        }
        String clientId = client.getId();
        this.saleService.createSale(sellerId, clientId);
        log.info("Venta creada con éxito");
        return new ResponseEntity<>(new Message("Compra exitosa"), HttpStatus.OK);
    }
}
