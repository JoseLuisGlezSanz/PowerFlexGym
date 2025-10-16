package com.example.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.proyecto.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}