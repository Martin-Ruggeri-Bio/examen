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
public class SaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String sellerToken;

    private String clientToken;

    private String clientJson;

    private String sellerJson;

    private String dateRangeJson;

    private String productJson;

    private String shoppingCart;

    private String response_product;

    private String response_client_login;

    @Before
    public void setUp() throws Exception {
        sellerJson = "{"
        + "\"userName\":\"sellerSaleName\","
        + "\"role\":\"seller\""
        + "}";

        clientJson = "{"
        + "\"userName\":\"clientSaleName\","
        + "\"role\":\"client\""
        + "}";

        dateRangeJson = "{"
        + "\"fechaInicio\":\"2022-01-01T00:00:00Z\","
        + "\"fechaFin\":\"2025-01-01T00:00:00Z\""
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

        MvcResult client_login = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson))
                .andExpect(status().isOk())
                .andReturn();
        
        response_client_login = client_login.getResponse().getContentAsString();
        clientToken = "Bearer " + objectMapper.readTree(response_client_login).get("token").asText();

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

        mockMvc.perform(post("/shoppingList/addProduct")
                .header("Authorization", clientToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(shoppingCart))
                .andExpect(status().isOk());

    }


    @Test
    public void testCreateSale() throws Exception {
        mockMvc.perform(post("/sale/create")
                .header("Authorization", sellerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(response_client_login))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(post("/sale/create")
                .header("Authorization", sellerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(response_client_login))
                .andExpect(status().isOk());

        mockMvc.perform(get("/sale/all")
                .header("Authorization", sellerToken))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testGetByClient() throws Exception {
        mockMvc.perform(post("/sale/create")
                .header("Authorization", sellerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(response_client_login))
                .andExpect(status().isOk());

        mockMvc.perform(post("/sale/client")
                .header("Authorization", sellerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(response_client_login))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetBySeller() throws Exception {
        mockMvc.perform(post("/sale/create")
                .header("Authorization", sellerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(response_client_login))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/sale/seller")
                .header("Authorization", sellerToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindByDateBetween() throws Exception {
        mockMvc.perform(post("/sale/create")
            .header("Authorization", sellerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(response_client_login))
            .andExpect(status().isOk());

        mockMvc.perform(post("/sale/date-between")
                .header("Authorization", sellerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(dateRangeJson))
                .andExpect(status().isOk());
    }
}