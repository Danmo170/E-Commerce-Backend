package com.projects.ecommerce.model.dto.Order;

import com.projects.ecommerce.model.entity.Order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private Long id;

    private OrderStatus orderStatus;

    private List<OrderItemResponseDTO> items;

    private BigDecimal total;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
