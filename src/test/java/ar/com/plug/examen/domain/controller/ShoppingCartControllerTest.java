package ar.com.plug.examen.domain.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    private String productJson;

    private String shoppingCart;

    private String response_product;

    private String userLoginRequestJson;

    private String sellerJson;

    private String sellerToken;

    //obtener token
    @Before
    public void setUp() throws Exception {
        userLoginRequestJson = "{"
        + "\"userName\":\"testUserName\","
        + "\"role\":\"client\""
        + "}";

        sellerJson = "{"
        + "\"userName\":\"sellerSaleName\","
        + "\"role\":\"seller\""
        + "}";

        productJson = "{"
        + "\"nombre\":\"Super Hamburguesa\","
        + "\"descripcion\":\"Hamburguesa con jamon, queso, palta y lluvia de papas\","
        + "\"precio\":850.0,"
        + "\"urlImagen\":\"https://cdn.cienradios.com/wp-content/uploads/sites/13/2020/04/Panchos-argentinos-nota.jpg\","
        + "\"activo\":true,"
        + "\"creado\":\"2024-03-29T12:01:00Z\","
        + "\"actualizado\":\"2024-03-29T12:01:00Z\""
        + "}";

        MvcResult seller_login = mockMvc.perform(post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(sellerJson))
        .andExpect(status().isOk())
        .andReturn();

        String response_seller_login = seller_login.getResponse().getContentAsString();
        sellerToken = "Bearer " + objectMapper.readTree(response_seller_login).get("token").asText();

        MvcResult result_login = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userLoginRequestJson))
                .andExpect(status().isOk())
                .andReturn();

        String response_login = result_login.getResponse().getContentAsString();
        token = "Bearer " + objectMapper.readTree(response_login).get("token").asText();

        MvcResult result_product = mockMvc.perform(post("/product/create")
                                            .header("Authorization", sellerToken)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(productJson))
                                            .andReturn();
        
        response_product = result_product.getResponse().getContentAsString();

        shoppingCart = "{"
        + "\"product\":" + response_product + ","
        + "\"amount\":1"
        + "}";
    }

    @Test
    public void testGetListByClient() throws Exception {

        mockMvc.perform(get("/shoppingList")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void testCountByClient() throws Exception {

        mockMvc.perform(get("/shoppingList/count")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddProduct() throws Exception {

        mockMvc.perform(post("/shoppingList/addProduct")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(shoppingCart))
                .andExpect(status().isOk());
    }

    @Test
    public void testCleanProduct() throws Exception {

        mockMvc.perform(post("/shoppingList/addProduct")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(shoppingCart))
                .andExpect(status().isOk());

        String itemId = objectMapper.readTree(response_product).get("id").asText();
        
        mockMvc.perform(delete("/shoppingList/clean/" + itemId)
                .header("Authorization", token))
                .andExpect(status().isOk());
    }
}
