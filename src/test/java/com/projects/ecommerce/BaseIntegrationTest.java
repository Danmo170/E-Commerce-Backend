package com.projects.ecommerce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.ecommerce.model.dto.Cart.CartItemRequestDTO;
import com.projects.ecommerce.model.dto.Product.ProductRequestDTO;
import com.projects.ecommerce.model.entity.Product.Product;
import com.projects.ecommerce.model.entity.User.Role;
import com.projects.ecommerce.model.entity.User.User;
import com.projects.ecommerce.repository.*;
import com.projects.ecommerce.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected CartRepository cartRepository;

    @Autowired
    protected CartItemRepository cartItemRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderItemRepository orderItemRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected JwtUtil jwtUtil;

//  Tokens

    protected String getUserToken() {

        User user = User.builder()
                .name("User")
                .email("user@example.com")
                .password(passwordEncoder.encode("UserExample123"))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return jwtUtil.generateToken(user);

    }

    protected String getAdminToken() {

        User admin = User.builder()
                .name("Admin")
                .email("admin@example.com")
                .password(passwordEncoder.encode("AdminExample123"))
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);

        return jwtUtil.generateToken(admin);

    }

//  Helpers

    protected Product createTestProduct() {

        return productRepository.save(Product.builder()
                .name("Product")
                .description("Description")
                .price(BigDecimal.valueOf(1.5))
                .stock(5)
                .category("Tests")
                .build());

    }

    protected ProductRequestDTO createTestProductRequestDTO() {

        return new ProductRequestDTO("Product",
                "Description",
                BigDecimal.valueOf(1.5),
                5,
                "Tests");

    }

    protected CartItemRequestDTO createValidCartItemRequestDTO(Product product) {

        return new CartItemRequestDTO(product.getId(), product.getStock() - 1);

    }

    protected CartItemRequestDTO createOverstockedCartItemRequestDTO(Product product) {

        return new CartItemRequestDTO(product.getId(), product.getStock() + 1);

    }

    protected void addItemToCart(String userToken) throws Exception {

        Product product = createTestProduct();

        mockMvc.perform(post("/api/v1/cart/items")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createValidCartItemRequestDTO(product))))
                .andExpect(status().isOk());

    }

    protected long createOrderAndGetId(String userToken) throws Exception {

        String response = mockMvc.perform(post("/api/v1/orders")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();

    }


//  BeforeEach

    @BeforeEach
    void cleanDataBase() {

        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();

    }

}
