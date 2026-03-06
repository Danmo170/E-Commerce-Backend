package com.projects.ecommerce.controller;

import com.projects.ecommerce.model.dto.Cart.CartItemRequestDTO;
import com.projects.ecommerce.model.dto.Cart.CartItemUpdateRequestDTO;
import com.projects.ecommerce.model.dto.Cart.CartResponseDTO;
import com.projects.ecommerce.model.entity.User.User;
import com.projects.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(@AuthenticationPrincipal User user) {

        return ResponseEntity.ok(cartService.getCart(user));

    }

    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addItem(@AuthenticationPrincipal User user,
                                                   @Valid @RequestBody CartItemRequestDTO cartItemRequestDTO) {


        return ResponseEntity.ok(cartService.addItem(user, cartItemRequestDTO));

    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartResponseDTO> updateItem(@AuthenticationPrincipal User user,
                                                      @PathVariable Long productId,
                                                      @Valid @RequestBody CartItemUpdateRequestDTO cartItemUpdateRequestDTO) {

        return ResponseEntity.ok(cartService.updateItem(user, productId, cartItemUpdateRequestDTO));

    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponseDTO> removeItem(@AuthenticationPrincipal User user,
                                                      @PathVariable Long productId) {

        return ResponseEntity.ok(cartService.removeItem(user, productId));

    }

}
