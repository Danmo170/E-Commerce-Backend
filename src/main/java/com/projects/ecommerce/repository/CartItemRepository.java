package com.projects.ecommerce.repository;

import com.projects.ecommerce.model.entity.Cart;
import com.projects.ecommerce.model.entity.CartItem;
import com.projects.ecommerce.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

}
