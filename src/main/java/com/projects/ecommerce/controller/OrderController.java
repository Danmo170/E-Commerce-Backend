package com.projects.ecommerce.controller;

import com.projects.ecommerce.model.dto.Order.OrderResponseDTO;
import com.projects.ecommerce.model.entity.Order.OrderStatus;
import com.projects.ecommerce.model.entity.User.User;
import com.projects.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(@AuthenticationPrincipal User user,
                                                               @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<OrderResponseDTO> orders = orderService.getAllOrders(user, pageable);

        if (orders.isEmpty()) {

            return ResponseEntity.noContent().build();

        }

        return ResponseEntity.ok(orders);

    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@AuthenticationPrincipal User user,
                                                         @PathVariable Long id) {

        return ResponseEntity.ok(orderService.getOrderById(user, id));

    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@AuthenticationPrincipal User user) {

        OrderResponseDTO orderResponseDTO = orderService.createOrder(user);

        URI location = URI.create("/api/orders/" + orderResponseDTO.getId());

        return ResponseEntity.created(location).body(orderResponseDTO);

    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@AuthenticationPrincipal User user,
                                                        @PathVariable Long id) {

        return ResponseEntity.ok(orderService.cancelOrder(user, id));

    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Long id,
                                                              @RequestParam OrderStatus status) {

        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));

    }

}
