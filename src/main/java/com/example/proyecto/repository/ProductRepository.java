package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Buscar productos por nombre
    List<Product> findByNameContainingIgnoreCase(String name);
    
    // Buscar productos por estado
    List<Product> findByStatus(Integer status);
    
    // Buscar productos con stock bajo
    List<Product> findByStockLessThan(Integer stock);
    
    // Buscar productos por rango de precio
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
}