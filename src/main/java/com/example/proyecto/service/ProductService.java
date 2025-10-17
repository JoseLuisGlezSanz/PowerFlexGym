package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.ProductRequest;
import com.example.proyecto.dto.ProductResponse;

public interface ProductService {
    List<ProductResponse> findAll();
    ProductResponse findById(Integer id);
    ProductResponse save(ProductRequest productRequest);
    ProductResponse update(Integer id, ProductRequest productRequest);
    void delete(Integer id);
    List<ProductResponse> findByName(String name);
    List<ProductResponse> findByStatus(Integer status);
    List<ProductResponse> findByStockLessThan(Integer stock);
}