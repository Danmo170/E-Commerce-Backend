package com.projects.ecommerce.config;

import com.projects.ecommerce.model.entity.Product.Product;
import com.projects.ecommerce.model.entity.User.Role;
import com.projects.ecommerce.model.entity.User.User;
import com.projects.ecommerce.repository.ProductRepository;
import com.projects.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnBooleanProperty(name = "data-initializer.enabled", matchIfMissing = true)
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.count() == 0) {

            User user = User.builder()
                    .name("User")
                    .email("user@example.com")
                    .password(passwordEncoder.encode("UserExample123"))
                    .role(Role.USER)
                    .build();

            User admin = User.builder()
                    .name("Admin")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("AdminExample123"))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(user);
            userRepository.save(admin);

        }

        if (productRepository.count() == 0) {

            record ProductData(String name, String description, BigDecimal price, int stock, String category) {}

            List<ProductData> products = List.of(
                    new ProductData("MacBook Pro 14\"", "Apple M3 Pro chip, 18GB RAM, 512GB SSD", BigDecimal.valueOf(1999.99), 10, "Electronics"),
                    new ProductData("Sony WH-1000XM5", "Industry-leading noise canceling headphones", BigDecimal.valueOf(349.99), 25, "Electronics"),
                    new ProductData("Nike Air Max 270", "Men's running shoes, lightweight and comfortable", BigDecimal.valueOf(129.99), 40, "Sports"),
                    new ProductData("Yoga Mat Pro", "Non-slip professional yoga mat 6mm", BigDecimal.valueOf(49.99), 60, "Sports"),
                    new ProductData("Clean Code", "A Handbook of Agile Software Craftsmanship by Robert C. Martin", BigDecimal.valueOf(39.99), 30, "Books"),
                    new ProductData("The Pragmatic Programmer", "Your journey to mastery, 20th Anniversary Edition", BigDecimal.valueOf(44.99), 25, "Books"),
                    new ProductData("Nespresso Vertuo", "Coffee and espresso machine with milk frother", BigDecimal.valueOf(179.99), 15, "Home"),
                    new ProductData("Logitech MX Master 3", "Advanced wireless mouse for Mac and PC", BigDecimal.valueOf(99.99), 35, "Electronics"),
                    new ProductData("Levi's 501 Jeans", "Original fit jeans, classic blue", BigDecimal.valueOf(69.99), 50, "Clothing"),
                    new ProductData("The North Face Jacket", "Waterproof and windproof outdoor jacket", BigDecimal.valueOf(249.99), 20, "Clothing")
            );

            products.forEach(data -> productRepository.save(
                    Product.builder()
                            .name(data.name())
                            .description(data.description())
                            .price(data.price())
                            .stock(data.stock())
                            .category(data.category())
                            .build()
            ));

        }

    }

}
