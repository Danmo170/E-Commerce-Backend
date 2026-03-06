package com.projects.ecommerce.model.dto.Cart;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemUpdateRequestDTO {

    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;

}
