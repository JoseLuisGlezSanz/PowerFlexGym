package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.RoleRequest;
import com.example.proyecto.dto.RoleResponse;

public interface RoleService {
    List<RoleResponse> findAll();

    RoleResponse findById(Integer id);

    RoleResponse create(RoleRequest request);

    RoleResponse update(Integer id, RoleRequest request);
    
    void delete(Integer id);
}
