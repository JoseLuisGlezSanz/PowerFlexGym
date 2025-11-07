package com.example.proyecto.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        return ProductMapper.toResponse(product);
    }

    @Override
    public ProductResponse create(ProductRequest productRequest) {
        Product product = ProductMapper.toEntity(productRequest);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest productRequest) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        
        ProductMapper.copyToEntity(productRequest, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        return ProductMapper.toResponse(updatedProduct);
    }

    // @Override
    // public void delete(Integer id) {
    //     productRepository.deleteById(id);
    // }

    @Override
    public List<ProductResponse> getAll(int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        Page<Product> products = productRepository.findAll(pageReq);
        return products.getContent().stream().map(ProductMapper::toResponse).toList();
    }

    @Override
    public List<ProductResponse> findByName(String name) {
          List<Product> products = productRepository.findByName(name);
          return products.stream().map(ProductMapper::toResponse).toList();
    }

    @Override
    public List<ProductResponse> findByStatus(Integer status) {
          List<Product> products = productRepository.findByStatus(status);
          return products.stream().map(ProductMapper::toResponse).toList();
    }

    @Override
    public List<ProductResponse> findByStockLessThan(Integer stock) {
          List<Product> products = productRepository.findByStockLessThan(stock);
          return products.stream().map(ProductMapper::toResponse).toList();
    }
}