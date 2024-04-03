package ar.com.plug.examen.domain.controller;

import ar.com.plug.examen.domain.model.Product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testFindAll() throws Exception {
        mockMvc.perform(get("/product/all"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindByIdWhenProductExist() throws Exception {
        Long id = 1L; // Use an id that exists in your test database

        mockMvc.perform(get("/product/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindByIdWhenProductDoesNotExist() throws Exception {
        Long id = 999L; // Use an id that does not exist in your test database

        mockMvc.perform(get("/product/" + id))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddWhenDataProductIsCorrect() throws Exception {
        String productJson = "{"
            + "\"nombre\":\"Super Hamburguesa\","
            + "\"descripcion\":\"Hamburguesa con jamon, queso, palta y lluvia de papas\","
            + "\"precio\":850.0,"
            + "\"urlImagen\":\"https://cdn.cienradios.com/wp-content/uploads/sites/13/2020/04/Panchos-argentinos-nota.jpg\","
            + "\"activo\":true,"
            + "\"creado\":\"2024-03-29T12:01:00Z\","
            + "\"actualizado\":\"2024-03-29T12:01:00Z\""
            + "}";

        mockMvc.perform(post("/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddWhenDataProductIsIncorrect() throws Exception {
        String productJson = "{\"name\":\"\",\"price\":-1}"; // JSON with incorrect data

        mockMvc.perform(post("/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteByIdWhenProductExist() throws Exception {
        Long id = 1L; // Use an id that exists in your test database

        mockMvc.perform(delete("/product/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteByIdWhenProductDoesNotExist() throws Exception {
        Long id = 999L; // Use an id that does not exist in your test database
        String productJson = "{\"name\":\"\",\"price\":-1}"; 

        mockMvc.perform(put("/product/update/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateWhenProductExist() throws Exception {
        Long id = 1L; // Use an id that exists in your test database
        Product product = new Product(); // Set the properties of the product

        mockMvc.perform(put("/product/update/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateWhenProductDoesNotExist() throws Exception {
        Long id = 999L; // Use an id that does not exist in your test database
        Product product = new Product(); // Set the properties of the product

        mockMvc.perform(put("/product/update/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isBadRequest());
    }
}