package com.projects.ecommerce.controller;

import com.projects.ecommerce.model.dto.Product.ProductRequestDTO;
import com.projects.ecommerce.model.dto.Product.ProductResponseDTO;
import com.projects.ecommerce.model.dto.Product.ProductUpdateRequestDTO;
import com.projects.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {

        List<ProductResponseDTO> products = productService.getAllProducts();

        if (products.isEmpty()) {

            return ResponseEntity.noContent().build();

        }

        return ResponseEntity.ok(products);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {

        return ResponseEntity.ok(productService.getProductById(id));

    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {

        ProductResponseDTO productResponseDTO = productService.createProduct(productRequestDTO);

        URI location = URI.create("/api/products/" + productResponseDTO.getId());

        return ResponseEntity.created(location).body(productResponseDTO);

    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id,
                                                            @Valid @RequestBody ProductUpdateRequestDTO productUpdateRequestDTO) {

        return ResponseEntity.ok(productService.updateProduct(id, productUpdateRequestDTO));

    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateProduct(@PathVariable Long id) {

        productService.deactivateProduct(id);

        return ResponseEntity.noContent().build();

    }

}
