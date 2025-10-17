package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Buscar productos por nombre
    List<Product> findByName(String name);
    
    // Buscar productos por estado
    List<Product> findByStatus(Integer status);
}