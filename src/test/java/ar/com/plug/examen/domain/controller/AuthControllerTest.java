package ar.com.plug.examen.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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

    private String clientLoginRequestJson;

    private String sellerLoginRequestJson;

    private String otroLoginRequestJson;

    @Before
    public void setUp() {
        clientLoginRequestJson = "{"
            + "\"userName\":\"testUserName\","
            + "\"role\":\"client\""
            + "}";
        
        sellerLoginRequestJson = "{"
            + "\"userName\":\"testUserName\","
            + "\"role\":\"seller\""
            + "}";
        
        otroLoginRequestJson = "{"
            + "\"userName\":\"testUserName\","
            + "\"role\":\"otro\""
            + "}";

    }

    @Test
    public void testLoginWhenUserIsClient() throws Exception {

        mockMvc.perform(post("/auth/login") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientLoginRequestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogoutWhenUserIsClient() throws Exception {

        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientLoginRequestJson))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        String token = "Bearer " + objectMapper.readTree(response).get("token").asText();

        mockMvc.perform(post("/auth/logout") 
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginWhenUserIsSeller() throws Exception {

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sellerLoginRequestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogoutWhenUserIsSeller() throws Exception {

        String response = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sellerLoginRequestJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String token = "Bearer " + objectMapper.readTree(response).get("token").asText();

        mockMvc.perform(post("/auth/logout") 
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginWhenRoleIsInvalid() throws Exception {

        mockMvc.perform(post("/auth/login") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(otroLoginRequestJson))
                .andExpect(status().isBadRequest());
    }

    @After
    public void tearDown() {
        clientLoginRequestJson = null;
        sellerLoginRequestJson = null;
        otroLoginRequestJson = null;
    }
}