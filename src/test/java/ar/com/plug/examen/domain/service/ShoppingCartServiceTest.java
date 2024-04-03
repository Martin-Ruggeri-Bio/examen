package ar.com.plug.examen.domain.service;

import ar.com.plug.examen.domain.model.ShoppingCart;
import ar.com.plug.examen.domain.model.Product;
import ar.com.plug.examen.domain.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShoppingCartServiceTest {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;


    @Test
    public void testAddProduct() {
        // Arrange
        ShoppingCart shoppingCart = new ShoppingCart();
        Product product = new Product();
        product.setNombre("nombre1");
        product.setPrecio(100.0F);
        User client = new User();

        Product added_product = productService.add(product);
        User added_client = userService.save(client);

        // Act
        ShoppingCart new_shoppingCart = shoppingCartService.addProduct(added_product, 2, added_client);

        // Assert
        assertNotNull(new_shoppingCart);
        assertNotEquals(shoppingCart, new_shoppingCart);
    }

    @Test
    public void testGetListByClient() {
        // Arrange
        User client = new User();
        client.setUserName("testUserName1");
        User added_client = userService.save(client);


        // Act
        List<ShoppingCart> shoppingCartList = shoppingCartService.getListByClient(added_client.getUserName());

        // Assert
        assertNotNull(shoppingCartList);
    }

    @Test
    public void testCleanShoppingCart() {
        // Arrange
        User client = new User();
        client.setUserName("testUserName2");
        User added_client = userService.save(client);

        Product product = new Product();
        product.setNombre("nombre1");
        product.setPrecio(100.0F);
        Product added_product = productService.add(product);

        shoppingCartService.addProduct(added_product, 2, added_client);

        // Act
        shoppingCartService.cleanShoppingCart(added_client.getUserName());
        List<ShoppingCart> shoppingCartList = shoppingCartService.getListByClient(added_client.getUserName());

        // Assert
        // evalua que la lista este vacia
        assertTrue(shoppingCartList.isEmpty());
    }

    @Test
    public void testGetCountByClient() {
        // Arrange
        Product product = new Product();
        product.setNombre("nombre1");
        product.setPrecio(100.0F);
        User client = new User();
        Product added_product = productService.add(product);
        User added_client = userService.save(client);

        shoppingCartService.addProduct(added_product, 1, added_client);

        Product product2 = new Product();
        product2.setNombre("nombre2");
        product2.setPrecio(200.0F);
        Product added_product2 = productService.add(product2);

        shoppingCartService.addProduct(added_product2, 2, added_client);

        // Act
        Long count = shoppingCartService.getCountByClient(added_client.getId());

        // Assert
        assertNotNull(count);
        assertEquals(2, count.longValue());
    }

    @Test
    public void testGetByClientAndProduct() {
        // Arrange
        Product product = new Product();
        product.setNombre("nombre1");
        product.setPrecio(100.0F);
        User client = new User();

        Product added_product = productService.add(product);
        User added_client = userService.save(client);

        ShoppingCart new_shoppingCart = shoppingCartService.addProduct(added_product, 2, added_client);

        // Act
        ShoppingCart shoppingCart = shoppingCartService.getByClientAndProduct(added_client.getId(), added_product.getId());

        // Assert
        assertNotNull(shoppingCart);
        assertEquals(new_shoppingCart, shoppingCart);
    }
}
