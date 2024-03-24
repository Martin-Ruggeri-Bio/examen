package ar.com.plug.examen.domain.controller;

import ar.com.plug.examen.domain.dtos.DateRange;
import ar.com.plug.examen.domain.dtos.Details;
import ar.com.plug.examen.domain.model.Detail;
import ar.com.plug.examen.domain.model.User;
import ar.com.plug.examen.domain.service.DetailService;
import ar.com.plug.examen.domain.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

@RestController
@RequestMapping("/saleDetail")
public class DetailController {

    @Autowired
    private DetailService detailService;

    @Autowired
	private UserService userService;

    @GetMapping("/client")
    public ResponseEntity<List<Detail>> getDetailsBySale(@RequestHeader(value="Authorization", required=true) String tokenHeader){
        String token = tokenHeader.replace("Bearer ", "");
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<User> userOptional = userService.getByToken(token);
        User user = userOptional.orElse(null);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return new ResponseEntity<>(this.detailService.getDetailsAll(), HttpStatus.OK);
    }

    // SaleControler
    @PostMapping("/date-between")
    public ResponseEntity<Details> findByDateBetween(@RequestHeader(value="Authorization", required=true) String tokenHeader,
                                                        @Valid @RequestBody DateRange dateRange, BindingResult bindingResult) {
        String token = tokenHeader.replace("Bearer ", "");
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<User> userOptional = userService.getByToken(token);
        User user = userOptional.orElse(null);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Details details = new Details(this.detailService.findByDateBetween(dateRange.getFechaInicio(), dateRange.getFechaFin()));
        return new ResponseEntity<>(details, HttpStatus.OK);
    }
}
