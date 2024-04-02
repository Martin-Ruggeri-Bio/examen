package ar.com.plug.examen.domain.service;

import java.util.Optional;

import ar.com.plug.examen.domain.model.User;
import ar.com.plug.examen.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getByUserName(String userName){
        log.info("Fetching user by username: {}", userName);
        return userRepository.findByUserName(userName);
    }
    
    public Optional<User> getById(String userId){
        log.info("Fetching user by ID: {}", userId);
        return userRepository.findById(userId);
    }

    public Optional<User> getByToken(String token){
        log.info("Fetching user by token: {}", token);
        return userRepository.findByToken(token);
    }
    
    public boolean existByUserName(String userName){
        log.info("Checking if user exists by username: {}", userName);
        return userRepository.existsByUserName(userName);
    }
    
    public User save(User user){
        log.info("Saving user: {}", user);
        return userRepository.save(user);
    }
    
    public void delete(User user){
        log.info("Deleting user: {}", user);
        userRepository.delete(user);
    }
}