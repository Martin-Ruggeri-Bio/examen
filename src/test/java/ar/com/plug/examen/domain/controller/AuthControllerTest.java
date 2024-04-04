package ar.com.plug.examen.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLoginWhenUserIsClient() throws Exception {
        String userLoginRequestJson = "{"
            + "\"userName\":\"testUserName\","
            + "\"role\":\"client\""
            + "}";

        mockMvc.perform(post("/auth/login") // replace with your actual login endpoint
                .contentType(MediaType.APPLICATION_JSON)
                .content(userLoginRequestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogoutWhenUserIsClient() throws Exception {
        // Login first to get the token
        String userLoginRequestJson = "{"
            + "\"userName\":\"testUserName\","
            + "\"role\":\"client\""
            + "}";

        String response = mockMvc.perform(post("/auth/login") // replace with your actual login endpoint
                .contentType(MediaType.APPLICATION_JSON)
                .content(userLoginRequestJson))
                .andReturn().getResponse().getContentAsString();

        String token = "Bearer " + objectMapper.readTree(response).get("token").asText();

        mockMvc.perform(post("/auth/logout") // replace with your actual logout endpoint
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginWhenUserIsSeller() throws Exception {
        String userLoginRequestJson = "{"
            + "\"userName\":\"testUserName\","
            + "\"role\":\"seller\""
            + "}";

        mockMvc.perform(post("/auth/login") // replace with your actual login endpoint
                .contentType(MediaType.APPLICATION_JSON)
                .content(userLoginRequestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogoutWhenUserIsSeller() throws Exception {
        // Login first to get the token
        String userLoginRequestJson = "{"
            + "\"userName\":\"testUserName\","
            + "\"role\":\"seller\""
            + "}";

        String response = mockMvc.perform(post("/auth/login") // replace with your actual login endpoint
                .contentType(MediaType.APPLICATION_JSON)
                .content(userLoginRequestJson))
                .andReturn().getResponse().getContentAsString();

        String token = "Bearer " + objectMapper.readTree(response).get("token").asText();

        mockMvc.perform(post("/auth/logout") // replace with your actual logout endpoint
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginWhenRoleIsInvalid() throws Exception {
        String userLoginRequestJson = "{"
            + "\"userName\":\"testUserName\","
            + "\"role\":\"otro\""
            + "}";

        mockMvc.perform(post("/auth/login") // replace with your actual login endpoint
                .contentType(MediaType.APPLICATION_JSON)
                .content(userLoginRequestJson))
                .andExpect(status().isBadRequest());
    }
}