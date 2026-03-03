package com.projects.ecommerce.service;

import com.projects.ecommerce.model.dto.ProductRequestDTO;
import com.projects.ecommerce.model.dto.ProductResponseDTO;
import com.projects.ecommerce.model.entity.Product;
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
                .orElseThrow(() -> new RuntimeException("Product not found"));

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

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        product.setPrice(productRequestDTO.getPrice());
        product.setStock(productRequestDTO.getStock());
        product.setCategory(productRequestDTO.getCategory());

        productRepository.save(product);

        return toProductResponseDTO(product);

    }

    public void deactivateProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

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
