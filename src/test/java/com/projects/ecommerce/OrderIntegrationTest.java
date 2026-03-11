package com.projects.ecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldCreateOrderSuccessfully() throws Exception {

        String userToken = getUserToken();

        addItemToCart(userToken);

        mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderStatus").value("PENDING"))
                .andExpect(jsonPath("$.id").exists());

    }

    @Test
    void shouldNotCreateOrderWithoutCart() throws Exception {

        mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer " + getUserToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldCancelOrderSuccessfully() throws Exception {

        String userToken = getUserToken();

        addItemToCart(userToken);

        long orderId = createOrderAndGetId(userToken);

        mockMvc.perform(patch("/api/orders/" + orderId + "/cancel")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus").value("CANCELLED"));


    }

    @Test
    void shouldNotCancelShippedOrder() throws Exception {

        String userToken = getUserToken();

        String adminToken = getAdminToken();

        addItemToCart(userToken);

        long orderId = createOrderAndGetId(userToken);

        mockMvc.perform(patch("/api/orders/" + orderId + "/status?status=SHIPPED")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/api/orders/" + orderId + "/cancel")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

}
