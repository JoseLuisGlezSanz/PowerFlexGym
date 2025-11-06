package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.CustomerRequest;
import com.example.proyecto.dto.CustomerResponse;

public interface CustomerService {
    List<CustomerResponse> findAll();
    CustomerResponse findById(Long id);
    CustomerResponse create(CustomerRequest customerRequest);
    CustomerResponse update(Long id, CustomerRequest customerRequest);
    // void delete(Long id);
    List<CustomerResponse> findByName(String name);
    List<CustomerResponse> findByVerifiedNumberTrue();
    List<CustomerResponse> findAllCustomersByGymId(Long gymId);
    List<CustomerResponse> getAll(int page, int pageSize);
    List<CustomerResponse> getByVerifiedNumberTrue(int page, int pageSize);
    List<CustomerResponse> getAllCustomersByGymId(int page, int pageSize, Long gymId);
}