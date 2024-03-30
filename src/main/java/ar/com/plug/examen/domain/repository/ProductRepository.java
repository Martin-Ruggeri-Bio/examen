package ar.com.plug.examen.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import ar.com.plug.examen.domain.model.Product;

/**
 * @author Martin
 * JpaRepository trae todas las operraciones basicas de un crud
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	public Optional<Product> findById(Long id);

	@Modifying
	public void deleteById(Long id);

}
