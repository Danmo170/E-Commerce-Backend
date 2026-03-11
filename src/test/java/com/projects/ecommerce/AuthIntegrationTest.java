package com.projects.ecommerce;

import com.projects.ecommerce.model.dto.Auth.LoginRequestDTO;
import com.projects.ecommerce.model.dto.Auth.RegisterRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class AuthIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {

        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("User", "user@example.com", "UserExample123");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

    }

    @Test
    void shouldLoginSuccessfully() throws Exception {

        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("User", "user@example.com", "UserExample123");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequestDTO)));

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("user@example.com", "UserExample123");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

    }

}
