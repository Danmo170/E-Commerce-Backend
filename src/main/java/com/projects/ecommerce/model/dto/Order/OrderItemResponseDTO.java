package com.projects.ecommerce.model.dto.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDTO {

    private Long productId;

    private String productName;

    private BigDecimal price;

    private int quantity;

    private BigDecimal subTotal;

}
