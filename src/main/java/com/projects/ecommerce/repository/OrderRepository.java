package com.projects.ecommerce.repository;

import com.projects.ecommerce.model.entity.Order.Order;
import com.projects.ecommerce.model.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

}
