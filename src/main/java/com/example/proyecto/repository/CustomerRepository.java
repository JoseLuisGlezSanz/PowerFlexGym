package com.example.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.proyecto.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {    
    // Buscar clientes por nombre
    List<Customer> findByName(String name);
    
    // Buscar clientes con número verificado
    List<Customer> findByVerifiedNumberTrue();
}