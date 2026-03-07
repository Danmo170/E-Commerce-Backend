package com.projects.ecommerce.service;

import com.projects.ecommerce.exception.BadRequestException;
import com.projects.ecommerce.exception.ResourceNotFoundException;
import com.projects.ecommerce.model.dto.Cart.CartItemRequestDTO;
import com.projects.ecommerce.model.dto.Cart.CartItemResponseDTO;
import com.projects.ecommerce.model.dto.Cart.CartItemUpdateRequestDTO;
import com.projects.ecommerce.model.dto.Cart.CartResponseDTO;
import com.projects.ecommerce.model.entity.Cart.Cart;
import com.projects.ecommerce.model.entity.Cart.CartItem;
import com.projects.ecommerce.model.entity.Product.Product;
import com.projects.ecommerce.model.entity.User.User;
import com.projects.ecommerce.repository.CartItemRepository;
import com.projects.ecommerce.repository.CartRepository;
import com.projects.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartResponseDTO getCart(User user) {

        return toCartResponseDTO(getOrCreateCart(user));

    }

    public CartResponseDTO addItem(User user, CartItemRequestDTO cartItemRequestDTO) {

        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findById(cartItemRequestDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.isActive()) {

            throw new BadRequestException("Product is not available");

        }

        if (cartItemRequestDTO.getQuantity() > product.getStock()) {

            throw new BadRequestException("Insufficient stock");

        }

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseGet(() ->

                    CartItem.builder()
                            .cart(cart)
                            .product(product)
                            .build()

                );

        cartItem.setQuantity(cartItem.getQuantity() + cartItemRequestDTO.getQuantity());

        cartItemRepository.save(cartItem);

        return toCartResponseDTO(cart);

    }

    public CartResponseDTO updateItem(User user, Long productId, CartItemUpdateRequestDTO cartItemUpdateRequestDTO) {

        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.isActive()) {

            throw new BadRequestException("Product is not available");

        }

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

        cartItem.setQuantity(cartItemUpdateRequestDTO.getQuantity());

        cartItemRepository.save(cartItem);

        return toCartResponseDTO(cart);

    }

    public CartResponseDTO removeItem(User user, Long productId) {

        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

        cartItemRepository.delete(cartItem);

        Cart updatedCart = cartRepository.findById(cart.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        return toCartResponseDTO(updatedCart);

    }

    private Cart getOrCreateCart(User user) {

        return cartRepository.findByUser(user)
                .orElseGet(() -> {

                    Cart newCart = Cart.builder()
                            .user(user)
                            .build();

                    return cartRepository.save(newCart);

                });

    }

    private CartResponseDTO toCartResponseDTO(Cart cart) {

        List<CartItemResponseDTO> items = cart.getItems().stream()
                .map(this::toCartItemResponseDTO)
                .toList();

        BigDecimal total = items.stream()
                .map(CartItemResponseDTO::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDTO(
                cart.getId(),
                items,
                total
        );

    }

    private CartItemResponseDTO toCartItemResponseDTO(CartItem cartItem) {

        return new CartItemResponseDTO(
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getPrice(),
                cartItem.getQuantity(),
                cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))
        );

    }

}
