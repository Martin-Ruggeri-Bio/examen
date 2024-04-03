package ar.com.plug.examen.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.plug.examen.domain.exception.ProductNotFoundException;
import ar.com.plug.examen.domain.model.Product;
import ar.com.plug.examen.domain.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;


/**
 * @author Martin
 *
 */
@Service
@Transactional
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Product add(Product product) {
        log.info("Adding new product: {}", product);
        if (product.getNombre() == null || product.getNombre().isEmpty() || product.getPrecio() == null || product.getPrecio() < 0) {
            log.error("Invalid product data: {}", product);
            throw new IllegalArgumentException("Invalid product data");
        }
        return repository.save(product);
    }

    public List<Product> findAll() {
        log.info("Fetching all products");
        return repository.findAll();
    }

    public Product findById(Long id) {
        log.info("Fetching product by ID: {}", id);
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Boolean deleteById(Long id) {
        log.info("Deleting product by ID: {}", id);
        if (repository.findById(id).isEmpty()) {
            log.warn("Product with ID {} not found for deletion", id);
            return false;
        }
        repository.deleteById(id);
        log.info("Product with ID {} deleted successfully", id);
        return !repository.findById(id).isPresent();
    }

    public Product update(Product newProduct, Long id) {
        log.info("Updating product with ID: {}", id);
        return repository.findById(id).map(product -> {
            product = new Product(
                    id,
                    newProduct.getNombre(),
                    newProduct.getDescripcion(),
                    newProduct.getPrecio(),
                    newProduct.getUrlImagen(),
                    newProduct.getActivo(),
                    newProduct.getCreado(),
                    newProduct.getActualizado()
            );
            Product updatedProduct = repository.save(product);
            log.info("Product updated successfully: {}", updatedProduct);
            return updatedProduct;
        }).orElseThrow(() -> new ProductNotFoundException(id));
    }
}