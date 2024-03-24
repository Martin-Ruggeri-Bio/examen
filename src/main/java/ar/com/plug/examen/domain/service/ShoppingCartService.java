package ar.com.plug.examen.domain.service;

import ar.com.plug.examen.domain.model.ShoppingCart;
import ar.com.plug.examen.domain.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    public List<ShoppingCart> getListByClient(String clientId){
        return this.shoppingCartRepository.findByClient_Id(clientId);
    }
    public void cleanShoppingCart(String clientId){
        this.shoppingCartRepository.deleteByClient_Id(clientId);
    }
    public void removeProduct(String id){
        this.shoppingCartRepository.deleteById(id);
    }
    public void addProduct(ShoppingCart shoppingCart){
        this.shoppingCartRepository.save(shoppingCart);
    }
    public Long getCountByClient(String clientId){
        return this.shoppingCartRepository.countByClient_Id(clientId);
    }

    public void updateProduct(String id, ShoppingCart shoppingCart){
        this.shoppingCartRepository.deleteById(id);
        this.shoppingCartRepository.save(shoppingCart);
    }

    public ShoppingCart getByClientAndProduct(String clientId, Integer productId){
        return this.shoppingCartRepository.findFirstByClient_IdAndProduct_Id(clientId, productId);
    }
}
