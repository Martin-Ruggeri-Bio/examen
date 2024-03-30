package ar.com.plug.examen.domain.service;

import ar.com.plug.examen.domain.model.ShoppingCart;
import ar.com.plug.examen.domain.repository.ShoppingCartRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    public List<ShoppingCart> getListByClient(String clientId){
        log.info("Fetching shopping cart items for client with ID: {}", clientId);
        return this.shoppingCartRepository.findByClient_Id(clientId);
    }
    
    public void cleanShoppingCart(String clientId){
        log.info("Cleaning shopping cart for client with ID: {}", clientId);
        this.shoppingCartRepository.deleteByClient_Id(clientId);
    }
    
    public void removeProduct(String id){
        log.info("Removing product from shopping cart with ID: {}", id);
        this.shoppingCartRepository.deleteById(id);
    }
    
    public void addProduct(ShoppingCart shoppingCart){
        log.info("Adding product to shopping cart: {}", shoppingCart);
        this.shoppingCartRepository.save(shoppingCart);
    }
    
    public Long getCountByClient(String clientId){
        log.info("Getting count of items in shopping cart for client with ID: {}", clientId);
        return this.shoppingCartRepository.countByClient_Id(clientId);
    }

    public void updateProduct(String id, ShoppingCart shoppingCart){
        log.info("Updating product in shopping cart with ID: {}", id);
        this.shoppingCartRepository.deleteById(id);
        this.shoppingCartRepository.save(shoppingCart);
    }

    public ShoppingCart getByClientAndProduct(String clientId, Long productId){
        log.info("Fetching shopping cart item for client with ID: {} and product with ID: {}", clientId, productId);
        return this.shoppingCartRepository.findFirstByClient_IdAndProduct_Id(clientId, productId);
    }
}
