package com.projects.ecommerce.service;

import com.projects.ecommerce.model.dto.Order.OrderItemResponseDTO;
import com.projects.ecommerce.model.dto.Order.OrderResponseDTO;
import com.projects.ecommerce.model.entity.Cart.Cart;
import com.projects.ecommerce.model.entity.Cart.CartItem;
import com.projects.ecommerce.model.entity.Order.Order;
import com.projects.ecommerce.model.entity.Order.OrderItem;
import com.projects.ecommerce.model.entity.Order.OrderStatus;
import com.projects.ecommerce.model.entity.User.User;
import com.projects.ecommerce.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public List<OrderResponseDTO> getAllOrders(User user) {

        return orderRepository.findByUser(user).stream()
                .map(this::toOrderResponseDTO)
                .toList();

    }

    public OrderResponseDTO getOrderById(User user, Long id) {

        return orderRepository.findByUserAndId(user, id)
                .map(this::toOrderResponseDTO)
                .orElseThrow(() -> new RuntimeException("Order not found"));

    }

    public OrderResponseDTO createOrder(User user) {

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {

            throw new RuntimeException("Cart has no items");

        }

        for (CartItem cartItem : cart.getItems()) {

            if (cartItem.getQuantity() > cartItem.getProduct().getStock()) {

                throw new RuntimeException("Insufficient stock");

            }

        }

        Order order = new Order();
        order.setUser(user);

        orderRepository.save(order);

        for (CartItem cartItem : cart.getItems()) {

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtMoment(cartItem.getProduct().getPrice());

            cartItem.getProduct().setStock(cartItem.getProduct().getStock() - cartItem.getQuantity());

            productRepository.save(cartItem.getProduct());
            orderItemRepository.save(orderItem);

        }

        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);

        orderRepository.save(order);
        cartItemRepository.deleteByCart(cart);

        return toOrderResponseDTO(order);

    }

    public OrderResponseDTO cancelOrder(User user, Long id) {

        Order order = orderRepository.findByUserAndId(user, id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {

            throw new RuntimeException("Order cannot be cancelled");

        }

        for (OrderItem orderItem : order.getItems()) {

            orderItem.getProduct().setStock(orderItem.getProduct().getStock() + orderItem.getQuantity());

            productRepository.save(orderItem.getProduct());

        }

        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);

        return toOrderResponseDTO(order);

    }

    public OrderResponseDTO updateOrderStatus(Long id, OrderStatus orderStatus) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(orderStatus);

        orderRepository.save(order);

        return toOrderResponseDTO(order);

    }

    private OrderResponseDTO toOrderResponseDTO(Order order) {

        List<OrderItemResponseDTO> items = order.getItems().stream()
                .map(this::toOrderItemResponseDTO)
                .toList();

        return new OrderResponseDTO(
                order.getId(),
                order.getStatus(),
                items,
                order.getTotal(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );

    }

    private OrderItemResponseDTO toOrderItemResponseDTO(OrderItem orderItem) {

        return new OrderItemResponseDTO(
                orderItem.getProduct().getId(),
                orderItem.getProduct().getName(),
                orderItem.getPriceAtMoment(),
                orderItem.getQuantity(),
                orderItem.getPriceAtMoment().multiply(BigDecimal.valueOf(orderItem.getQuantity()))
        );

    }

}
