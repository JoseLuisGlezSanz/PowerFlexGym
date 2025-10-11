package com.example.proyecto.service;

import java.util.List;
import com.example.proyecto.dto.ProductRequest;
import com.example.proyecto.dto.ProductResponse;

public interface ProductService {
    List<ProductResponse> findAll();
    ProductResponse findById(Integer id);
    ProductResponse create(ProductRequest request);
    ProductResponse update(Integer id, ProductRequest request);
    void delete(Integer id);
}