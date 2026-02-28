package com.projects.ecommerce.repository;

import com.projects.ecommerce.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
