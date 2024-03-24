package ar.com.plug.examen.domain.service;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.plug.examen.domain.exception.ProductNotFoundException;
import ar.com.plug.examen.domain.model.Product;
import ar.com.plug.examen.domain.repository.ProductRepository;


/**
 * @author Martin
 *
 */
@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductRepository repository;

	public Product add(Product product) {
		return repository.save(product);
	}

	public List<Product> findAll() {
		return repository.findAll();
	}

	public Product findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
	}

	public Boolean deleteById(Integer id) {
		if (repository.findById(id).isEmpty()) {
			return false;
		}
		repository.deleteById(id);
		return !repository.findById(id).isPresent();
	}

	public Product update(Product newProduct, Integer id) {
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
			return repository.save(product);
		}).orElseThrow(() -> new ProductNotFoundException(id));
	}

}

