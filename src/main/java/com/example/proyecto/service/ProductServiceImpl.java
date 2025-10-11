package com.example.proyecto.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.ProductRequest;
import com.example.proyecto.dto.ProductResponse;
import com.example.proyecto.model.Product;
import com.example.proyecto.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductResponse> findById(Integer id) {
        return productRepository.findById(id)
                .map(this::mapToResponse);
    }

    @Override
    public List<ProductResponse> findByNameContaining(String name) {
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> findByStatus(Integer status) {
        return productRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> findLowStock(Integer threshold) {
        return productRepository.findByStockLessThan(threshold).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> findByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse create(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .stock(request.getStock())
                .status(request.getStatus())
                .photo(request.getPhoto())
                .build();

        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }

    @Override
    public ProductResponse update(Integer id, ProductRequest request) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        existing.setName(request.getName());
        existing.setPrice(request.getPrice());
        existing.setStock(request.getStock());
        existing.setStatus(request.getStatus());
        existing.setPhoto(request.getPhoto());

        Product updated = productRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return productRepository.existsById(id);
    }

    @Override
    public ProductResponse updateStock(Integer id, Integer newStock) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (newStock < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }

        product.setStock(newStock);
        Product updated = productRepository.save(product);
        return mapToResponse(updated);
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .idProduct(product.getIdProduct())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .status(product.getStatus())
                .photo(product.getPhoto())
                .build();
    }
}