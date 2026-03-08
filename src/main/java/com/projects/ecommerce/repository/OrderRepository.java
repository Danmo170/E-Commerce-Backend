package com.projects.ecommerce.repository;

import com.projects.ecommerce.model.entity.Order.Order;
import com.projects.ecommerce.model.entity.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUser(User user, Pageable pageable);

    Optional<Order> findByUserAndId(User user, Long id);

}
