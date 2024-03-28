package ar.com.plug.examen.domain.service;

import ar.com.plug.examen.domain.model.Detail;
import ar.com.plug.examen.domain.model.Sale;
import ar.com.plug.examen.domain.model.ShoppingCart;
import ar.com.plug.examen.domain.repository.SaleRepository;
import lombok.extern.slf4j.Slf4j;
import ar.com.plug.examen.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
@Slf4j
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private DetailService detailService;


    public List<Sale> getSalesAll(){
        return this.saleRepository.findAll();
    }

    public List<Sale> getSalesByClient(String clientId){
        return this.saleRepository.findByClient_Id(clientId);
    }

    public List<Sale> findByDateBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return saleRepository.findByDateBetween(fechaInicio, fechaFin);
    }

    // generar ventas de acuerdo a los productos que el cliente tenga en su carrito
    public void createSale(String sellerId, String clientId){
        log.info("Creating sale for client with ID: {}", clientId);
        User seller = this.userService.getById(sellerId).get();
        User client = this.userService.getById(clientId).get();
        // obtengo la lista de productos del carrito del cliente
        List<ShoppingCart> shoppingCartList = this.shoppingCartService.getListByClient(clientId);
        // establecemos el formato decimal, para no tener tantos decimales
        DecimalFormat decimalFormat = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        // calculamos el total
        // .stream(): devuelve un stream (flujo o secuencia) de elementos que pueden ser procesados uno a uno de forma eficiente de la lista shoppingCartList.
        // .mapToDouble(shoppingCartItem -> shoppingCartItem.getProduct().getPrecio()): este método se aplica a cada elemento del stream y devuelve un stream que contiene el precio de cada artículo.
        //      Para hacer esto, el método mapToDouble utiliza una función lambda que toma cada elemento de la lista (un objeto shoppingCartItem) y devuelve su precio como un valor double.
        // .sum(): finalmente, se llama al método sum en el stream resultante para calcular la suma total de los precios de todos los artículos en el carrito.
        //      El resultado se almacena en la variable total
        double total = shoppingCartList.stream().mapToDouble(shoppingCartItem -> shoppingCartItem.getProduct().getPrecio()
                * shoppingCartItem.getAmount()).sum();
        log.info("Total sale amount calculated: {}", decimalFormat.format(total));
        LocalDateTime date = LocalDateTime.now();
        Sale sale = new Sale(Double.parseDouble(decimalFormat.format(total)), date, client, seller);
        log.info("Sale generated: {}", sale);
        Sale saveSale = this.saleRepository.save(sale);
        log.info("Sale saved in database: {}", saveSale);
        // creo un detalle cor cada item del carrito
        for (ShoppingCart shoppingCart : shoppingCartList) {
            Integer amountDetail = shoppingCart.getAmount();
            for (int index = 0; index < amountDetail; index++) {
                Detail detail = new Detail();
                detail.setProduct(shoppingCart.getProduct().getId());
                detail.setFecha(date);
                detail.setVentaId(saveSale.getId());
                detail.setPrecio(shoppingCart.getProduct().getPrecio());
                this.detailService.createDetail(detail);
            }
        }
        log.info("Sale details created and saved successfully");
        this.shoppingCartService.cleanShoppingCart(clientId);
        log.info("Shopping cart cleaned for client with ID: {}", clientId);
    }

    public List<Sale> getSalesBySeller(String sellerId) {
        log.info("Fetching sales for seller with ID: {}", sellerId);
        return this.saleRepository.findBySeller_Id(sellerId);
    }
}
