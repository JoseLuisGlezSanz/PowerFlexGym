package com.example.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.ProductRequest;
import com.example.proyecto.dto.ProductResponse;
import com.example.proyecto.mapper.ProductMapper;
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
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse findById(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        return ProductMapper.toResponse(product);
    }

    @Override
    public ProductResponse save(ProductRequest req) {
        Product product = ProductMapper.toEntity(req);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse update(Integer id, ProductRequest req) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        
        ProductMapper.copyToEntity(req, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        return ProductMapper.toResponse(updatedProduct);
    }

    @Override
    public void delete(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponse> findByName(String name) {
        return productRepository.findByName(name).stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> findByStatus(Integer status) {
        return productRepository.findByStatus(status).stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> findByStockLessThan(Integer stock) {
        return productRepository.findByStockLessThan(stock).stream()
                .map(ProductMapper::toResponse)
                .toList();
    }
}