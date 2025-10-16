package com.example.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.proyecto.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}