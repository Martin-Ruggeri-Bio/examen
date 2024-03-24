package ar.com.plug.examen.domain.service;

import java.util.Optional;

import ar.com.plug.examen.domain.model.User;
import ar.com.plug.examen.domain.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getByUserName(String userName){
        return userRepository.findByUserName(userName);
    }
    public Optional<User> getById(String userId){
        return userRepository.findById(userId);
    }

    public Optional<User> getByToken(String token){
        return userRepository.findByToken(token);
    }
    public boolean existByUserName(String userName){
        return userRepository.existsByUserName(userName);
    }
    public void save(User user){
        userRepository.save(user);
    }
    public void delete(User user){
        userRepository.delete(user);
    }
    
}
