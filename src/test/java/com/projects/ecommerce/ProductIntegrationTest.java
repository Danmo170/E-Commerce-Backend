package com.projects.ecommerce;

import com.projects.ecommerce.model.entity.Product.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ProductIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldReturnProductsSuccessfully() throws Exception {

        productRepository.save(Product.builder()
                .name("Product")
                .description("Description")
                .price(BigDecimal.valueOf(1.5))
                .stock(5)
                .category("Tests")
                .build());

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk());

    }

    @Test
    void shouldCreateProductSuccessfully() throws Exception {

        mockMvc.perform(post("/api/v1/products")
                        .header("Authorization", "Bearer " + getAdminToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTestProductRequestDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Product"))
                .andExpect(jsonPath("$.price").value(1.5));

    }

    @Test
    void shouldNotCreateProductWithUserToken() throws Exception {

        mockMvc.perform(post("/api/v1/products")
                        .header("Authorization", "Bearer " + getUserToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTestProductRequestDTO())))
                .andExpect(status().isForbidden());

    }

}
