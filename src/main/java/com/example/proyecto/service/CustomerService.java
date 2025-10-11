package com.example.proyecto.service;

import java.util.List;
import java.util.Optional;

import com.example.proyecto.dto.CustomerRequest;
import com.example.proyecto.dto.CustomerResponse;

public interface CustomerService {
    List<CustomerResponse> findAll();
    Optional<CustomerResponse> findById(Integer id);
    List<CustomerResponse> findByGymId(Integer gymId);
    Optional<CustomerResponse> findByPhone(String phone);
    List<CustomerResponse> findByNameContaining(String name);
    CustomerResponse create(CustomerRequest request);
    CustomerResponse update(Integer id, CustomerRequest request);
    void delete(Integer id);
    boolean existsById(Integer id);
    Long countByGymId(Integer gymId);
}