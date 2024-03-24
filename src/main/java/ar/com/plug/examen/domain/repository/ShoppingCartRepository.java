package ar.com.plug.examen.domain.repository;

import ar.com.plug.examen.domain.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, String> {
    List<ShoppingCart> findByClient_Id(String clientId);
    List<ShoppingCart> findByClient_UserName(String clientEmail);
    // para vaciar el carrito del cliente una vez completada la venta
    void deleteByClient_Id(String clientId);
    // numero de items en su carrito
    Long countByClient_Id(String clientId);
    ShoppingCart findFirstByClient_IdAndProduct_Id(String clientId, Integer productId);
}
