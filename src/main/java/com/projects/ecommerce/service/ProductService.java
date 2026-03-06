package com.projects.ecommerce.service;

import com.projects.ecommerce.exception.ResourceNotFoundException;
import com.projects.ecommerce.model.dto.Product.ProductRequestDTO;
import com.projects.ecommerce.model.dto.Product.ProductResponseDTO;
import com.projects.ecommerce.model.dto.Product.ProductUpdateRequestDTO;
import com.projects.ecommerce.model.entity.Product.Product;
import com.projects.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponseDTO> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::toProductResponseDTO)
                .toList();

    }

    public ProductResponseDTO getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return toProductResponseDTO(product);
    }

    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {

        Product product = Product.builder()
                .name(productRequestDTO.getName())
                .description(productRequestDTO.getDescription())
                .price(productRequestDTO.getPrice())
                .stock(productRequestDTO.getStock())
                .category(productRequestDTO.getCategory())
                .build();

        productRepository.save(product);

        return toProductResponseDTO(product);

    }

    public ProductResponseDTO updateProduct(Long id, ProductUpdateRequestDTO productUpdateRequestDTO) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        if (productUpdateRequestDTO.getName() != null) product.setName(productUpdateRequestDTO.getName());
        if (productUpdateRequestDTO.getDescription() != null) product.setDescription(productUpdateRequestDTO.getDescription());
        if (productUpdateRequestDTO.getPrice() != null) product.setPrice(productUpdateRequestDTO.getPrice());
        if (productUpdateRequestDTO.getStock() != null) product.setStock(productUpdateRequestDTO.getStock());
        if (productUpdateRequestDTO.getCategory() != null) product.setCategory(productUpdateRequestDTO.getCategory());

        productRepository.save(product);

        return toProductResponseDTO(product);

    }

    public void deactivateProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setActive(false);

        productRepository.save(product);

    }

    private ProductResponseDTO toProductResponseDTO(Product product) {

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.isActive()
        );

    }

}
