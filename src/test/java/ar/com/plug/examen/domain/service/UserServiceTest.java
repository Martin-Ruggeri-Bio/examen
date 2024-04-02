package ar.com.plug.examen.domain.service;

import ar.com.plug.examen.domain.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testSaveUser() {
        // Arrange
        User user = new User();
        user.setUserName("username");
        user.setToken("token");
        user.setRole("role");

        // Act
        User savedUser = userService.save(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals(user, savedUser);
    }

    @Test
    public void testGetByUserName() {
        // Arrange
        User user = new User();
        user.setUserName("username1");
        user.setToken("token1");
        user.setRole("role");
        userService.save(user);

        // Act
        Optional<User> foundUser = userService.getByUserName(user.getUserName());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    public void testGetById() {
        // Arrange
        User user = new User();
        user.setUserName("username2");
        user.setToken("token2");
        user.setRole("role2");
        userService.save(user);

        // Act
        Optional<User> foundUser = userService.getById(user.getId());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    public void testGetByToken() {
        // Arrange
        User user = new User();
        user.setUserName("username3");
        user.setToken("token3");
        user.setRole("role3");
        userService.save(user);

        // Act
        Optional<User> foundUser = userService.getByToken(user.getToken());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    public void testExistByUserName() {
        // Arrange
        User user = new User();
        user.setUserName("username4");
        user.setToken("token4");
        user.setRole("role4");
        userService.save(user);

        // Act
        boolean exists = userService.existByUserName(user.getUserName());

        // Assert
        assertTrue(exists);
    }

    @Test
    public void testSaveAndDeleteUser() {
        // Arrange
        User user = new User();
        user.setUserName("username5");
        user.setToken("token5");
        user.setRole("role5");

        // Act
        userService.save(user);
        Optional<User> foundUser = userService.getByUserName(user.getUserName());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());

        // Act
        userService.delete(user);
        boolean exists = userService.existByUserName(user.getUserName());

        // Assert
        assertFalse(exists);
    }
}