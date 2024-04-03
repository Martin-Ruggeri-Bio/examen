package ar.com.plug.examen.domain.controller;

import ar.com.plug.examen.domain.dtos.UserLoginRequest;
import ar.com.plug.examen.domain.dtos.UserTokenResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @Test
    public void testLoginWhenUserIsClient() {
        // Arrange
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUserName("testUserName");
        userLoginRequest.setRole("client");

        // Act
        UserTokenResponse userTokenResponse = authController.login(userLoginRequest);

        // Assert
        assertNotNull(userTokenResponse);
        assertNotNull(userTokenResponse.getToken());
    }

    @Test
    public void testLogoutWhenUserIsClient() {
        // Arrange
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUserName("testUserName");
        userLoginRequest.setRole("client");

        UserTokenResponse userTokenResponse = authController.login(userLoginRequest);
        String token = "Bearer " + userTokenResponse.getToken();

        // Act
        ResponseEntity<Object> responseEntity = authController.logout(token);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testLoginWhenUserIsSeller() {
        // Arrange
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUserName("testUserName");
        userLoginRequest.setRole("seller");

        // Act
        UserTokenResponse userTokenResponse = authController.login(userLoginRequest);

        // Assert
        assertNotNull(userTokenResponse);
        assertNotNull(userTokenResponse.getToken());
    }

    @Test
    public void testLogoutWhenUserIsSeller() {
        // Arrange
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUserName("testUserName");
        userLoginRequest.setRole("seller");

        UserTokenResponse userTokenResponse = authController.login(userLoginRequest);
        String token = "Bearer " + userTokenResponse.getToken();

        // Act
        ResponseEntity<Object> responseEntity = authController.logout(token);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testLoginWhenRoleIsInvalid() {
        // Arrange
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUserName("testUserName");
        userLoginRequest.setRole("invalidRole");

        // Act
        try {
            authController.login(userLoginRequest);
        } catch (RuntimeException e) {
            // Assert
            assertEquals("Rol inválido", e.getMessage());
        }
    }
}