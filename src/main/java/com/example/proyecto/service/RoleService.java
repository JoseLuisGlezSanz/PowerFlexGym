package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.RoleRequest;
import com.example.proyecto.dto.RoleResponse;

public interface RoleService {
    List<RoleResponse> findAll();
    RoleResponse findById(Long id);
    RoleResponse create(RoleRequest roleRequest);
    RoleResponse update(Long id, RoleRequest roleRequest);
    // void delete(Long id);
    List<RoleResponse> findByStatus(Integer statusValue);
}