package com.projects.ecommerce;

import com.projects.ecommerce.model.entity.Product.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CartIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldAddItemToCartSuccessfully() throws Exception {

        Product product = createTestProduct();

        mockMvc.perform(post("/api/v1/cart/items")
                        .header("Authorization", "Bearer " + getUserToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createValidCartItemRequestDTO(product))))
                .andExpect(status().isOk());

    }

    @Test
    void shouldNotAddItemWithInsufficientStock() throws Exception {

        Product product = createTestProduct();

        mockMvc.perform(post("/api/v1/cart/items")
                        .header("Authorization", "Bearer " + getUserToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOverstockedCartItemRequestDTO(product))))
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldRemoveItemFromCartSuccessfully() throws Exception {

        Product product = createTestProduct();

        String userToken = getUserToken();

        mockMvc.perform(post("/api/v1/cart/items")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createValidCartItemRequestDTO(product))))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/v1/cart/items/" + product.getId())
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());

    }

}
