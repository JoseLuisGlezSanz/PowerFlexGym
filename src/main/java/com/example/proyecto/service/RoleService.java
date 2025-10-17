package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.RoleRequest;
import com.example.proyecto.dto.RoleResponse;

public interface RoleService {
    List<RoleResponse> findAll();
    RoleResponse findById(Integer id);
    RoleResponse save(RoleRequest roleRequest);
    RoleResponse update(Integer id, RoleRequest roleRequest);
    void delete(Integer id);
    List<RoleResponse> findByStatus(Integer status);
}