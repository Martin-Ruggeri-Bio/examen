package ar.com.plug.examen.domain.service;

import ar.com.plug.examen.domain.model.Product;
import ar.com.plug.examen.domain.model.Sale;
import ar.com.plug.examen.domain.model.User;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SaleServiceTest {

    @Autowired
    private SaleService saleService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    private User added_seller;

    private User added_client;

    private Product added_product;


    @Before
    public void setUp() {
        User seller = new User();
        User client = new User();
        added_seller = userService.save(seller);
        added_client = userService.save(client);

        Product product = new Product();
        product.setPrecio(100.0F);
        product.setNombre("Test Product");
        product.setDescripcion("Test Description");
        added_product = productService.add(product);

        shoppingCartService.addProduct(added_product, 1, added_client);
    }

    @Test
    public void testGetSalesAll() {
        // Act
        List<Sale> sales = saleService.getSalesAll();

        // Assert
        assertNotNull(sales);
    }

    @Test
    public void testGetSalesByClient() {
        // Arrange
        User client = new User();
        User added_client = userService.save(client);

        // Act
        List<Sale> sales = saleService.getSalesByClient(added_client.getId());

        // Assert
        assertNotNull(sales);
    }

    @Test
    public void testFindByDateBetween() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();

        // Act
        List<Sale> sales = saleService.findByDateBetween(startDate, endDate);

        // Assert
        assertNotNull(sales);
    }

    @Test
    public void testCreateSale() {
        shoppingCartService.addProduct(added_product, 1, added_client);

        // Act
        saleService.createSale(added_seller.getId(), added_client.getId());

        // Assert
        // Check if the sale was created successfully
        List<Sale> sales = saleService.getSalesByClient(added_client.getId());
        assertFalse(sales.isEmpty());
    }

    @Test
    public void testGetSalesBySeller() {
        // Arrange
        User seller = new User();
        User added_seller = userService.save(seller);

        // Act
        List<Sale> sales = saleService.getSalesBySeller(added_seller.getId());

        // Assert
        assertNotNull(sales);
    }
}