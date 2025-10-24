package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.CustomerRequest;
import com.example.proyecto.dto.CustomerResponse;

public interface CustomerService {
    List<CustomerResponse> findAll();
    CustomerResponse findById(Integer id);
    CustomerResponse save(CustomerRequest customerRequest);
    CustomerResponse update(Integer id, CustomerRequest customerRequest);
    List<CustomerResponse> findByName(String name);
    List<CustomerResponse> findByVerifiedNumberTrue();
    List<CustomerResponse> getAll(int page, int pageSize);
    List<CustomerResponse> getByVerifiedNumberTrue(int page, int pageSize);
    List<CustomerResponse> findByGymId(Integer idGym);
    List<CustomerResponse> getByGymId(int page, int pageSize, Integer gymId);
}