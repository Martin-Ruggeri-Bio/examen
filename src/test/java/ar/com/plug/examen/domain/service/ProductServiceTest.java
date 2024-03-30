package ar.com.plug.examen.domain.service;

import ar.com.plug.examen.domain.model.Product;
import ar.com.plug.examen.domain.exception.ProductNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    public void testAddProduct() {
        // Arrange
        Product product = new Product();
        product.setNombre("nombre");
        product.setDescripcion("descripcion");
        product.setPrecio(100.0F);
        product.setUrlImagen("urlImagen");
        product.setActivo(true);
        product.setCreado("2024-03-30T12:00:00Z");
        product.setActualizado("2024-03-30T12:00:00Z");
        // Act
        Product addedProduct = productService.add(product);

        // Assert
        assertNotNull(addedProduct);
        assertEquals(product, addedProduct);
    }

    @Test
    public void testFindAllProducts() {
        // Arrange
        List<Product> productList = productService.findAll();

        // Act
        List<Product> foundProducts = productService.findAll();

        // Assert
        assertNotNull(foundProducts);
        assertEquals(productList, foundProducts);
    }

    @Test
    public void testFindProductById() {
        // Arrange
        Product product = new Product();
        product.setNombre("Super Pancho");
        product.setDescripcion("Pancho con jamon, queso, palta y lluvia de papas");
        product.setPrecio(750.00F);
        product.setUrlImagen("https://cdn.cienradios.com/wp-content/uploads/sites/13/2020/04/Panchos-argentinos-nota.jpg");
        product.setActivo(true);
        product.setCreado("2024-03-30T12:00:00Z");
        product.setActualizado("2024-03-30T12:00:00Z");
        productService.add(product);

        // Act
        Product foundProduct = productService.findById(product.getId());

        // Assert
        assertNotNull(foundProduct);
        assertEquals(product, foundProduct);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testFindProductById_ProductNotFound() {
        // Arrange
        Long productId = 999L;

        // Act
        productService.findById(productId);

        // Assert
        // Expects ProductNotFoundException to be thrown
    }

    @Test
    public void testUpdateProduct() {
        // Arrange
        Product existingProduct = new Product();
        existingProduct.setNombre("nombre");
        existingProduct.setDescripcion("descripcion");
        existingProduct.setPrecio(100.0F);
        existingProduct.setUrlImagen("urlImagen");
        existingProduct.setActivo(true);
        existingProduct.setCreado("2024-03-30T12:00:00Z");
        existingProduct.setActualizado("2024-03-30T12:00:00Z");
        productService.add(existingProduct);

        Product newProduct = new Product();
        newProduct.setNombre("nuevo nombre");
        newProduct.setDescripcion("nueva descripcion");
        newProduct.setPrecio(200.0F);
        newProduct.setUrlImagen("nuevaUrlImagen");
        newProduct.setActivo(false);
        newProduct.setCreado("2024-03-30T12:00:00Z");
        newProduct.setActualizado("2024-03-30T12:00:00Z");
        // Act
        Product updatedProduct = productService.update(newProduct, existingProduct.getId());

        // Assert
        assertNotNull(updatedProduct);
        assertEquals(newProduct.getNombre(), updatedProduct.getNombre());
        assertEquals(newProduct.getDescripcion(), updatedProduct.getDescripcion());
        assertEquals(newProduct.getPrecio(), updatedProduct.getPrecio(), 0.01);
        assertEquals(newProduct.getUrlImagen(), updatedProduct.getUrlImagen());
        assertEquals(newProduct.getActivo(), updatedProduct.getActivo());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testUpdateProduct_ProductNotFound() {
        // Arrange
        Long id = 999L;
        Product newProduct = new Product();
        newProduct.setNombre("nuevo nombre");
        newProduct.setDescripcion("nueva descripcion");
        newProduct.setPrecio(200.0F);
        newProduct.setUrlImagen("nuevaUrlImagen");
        newProduct.setActivo(false);
        newProduct.setCreado("2024-03-30T12:00:00Z");
        newProduct.setActualizado("2024-03-30T12:00:00Z");

        // Act
        productService.update(newProduct, id);

        // Assert
        // Expects ProductNotFoundException to be thrown
    }

    @Test
    public void testDeleteProductById() {
        // Arrange
        Product product = new Product();
        product.setNombre("nombre");
        product.setDescripcion("descripcion");
        product.setPrecio(100.0F);
        product.setUrlImagen("urlImagen");
        product.setActivo(true);
        product.setCreado("2024-03-30T12:00:00Z");
        product.setActualizado("2024-03-30T12:00:00Z");
        productService.add(product);

        // Act
        Boolean deleted = productService.deleteById(product.getId());

        // Assert
        assertTrue(deleted);
    }
}