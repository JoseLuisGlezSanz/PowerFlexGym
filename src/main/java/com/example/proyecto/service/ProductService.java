package com.example.proyecto.service;

import java.util.List;
import java.util.Optional;

import com.example.proyecto.dto.ProductRequest;
import com.example.proyecto.dto.ProductResponse;

public interface ProductService {
    List<ProductResponse> findAll();
    Optional<ProductResponse> findById(Integer id);
    List<ProductResponse> findByNameContaining(String name);
    List<ProductResponse> findByStatus(Integer status);
    List<ProductResponse> findLowStock(Integer threshold);
    List<ProductResponse> findByPriceRange(Double minPrice, Double maxPrice);
    ProductResponse create(ProductRequest request);
    ProductResponse update(Integer id, ProductRequest request);
    void delete(Integer id);
    boolean existsById(Integer id);
    ProductResponse updateStock(Integer id, Integer newStock);
}