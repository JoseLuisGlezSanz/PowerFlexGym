package com.example.proyecto.service;

import java.util.List;
import java.util.Optional;

import com.example.proyecto.dto.RoleRequest;
import com.example.proyecto.dto.RoleResponse;

public interface RoleService {
    List<RoleResponse> findAll();
    Optional<RoleResponse> findById(Integer id);
    List<RoleResponse> findByStatus(Integer status);
    RoleResponse create(RoleRequest request);
    RoleResponse update(Integer id, RoleRequest request);
    void delete(Integer id);
    boolean existsById(Integer id);
}