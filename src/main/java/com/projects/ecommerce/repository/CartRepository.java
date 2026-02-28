package com.projects.ecommerce.repository;

import com.projects.ecommerce.model.entity.Cart;
import com.projects.ecommerce.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);

}
