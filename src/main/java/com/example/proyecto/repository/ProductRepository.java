package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.proyecto.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Buscar productos por nombre
    @Query(value = "SELECT * FROM products WHERE LOWER(name) = LOWER(:name);", nativeQuery = true)
    List<Product> findByName(@Param("name") String name);
    
    // Buscar productos por estado
    @Query(value = "SELECT * FROM products WHERE status = :status;", nativeQuery = true)
    List<Product> findByStatus(@Param("status") Integer status);

    // Buscar productos con poco inventario
    @Query(value = "SELECT * FROM products WHERE stock < :stock;", nativeQuery = true)
    List<Product> findByStockLessThan(@Param("stock") Integer stock);
}