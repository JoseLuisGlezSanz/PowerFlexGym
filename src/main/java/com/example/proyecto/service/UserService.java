package com.example.proyecto.service;

import java.util.List;
import java.util.Optional;

import com.example.proyecto.dto.UserRequest;
import com.example.proyecto.dto.UserResponse;

public interface UserService {
    List<UserResponse> findAll();
    Optional<UserResponse> findById(Integer id);
    Optional<UserResponse> findByUsername(String username);
    Optional<UserResponse> findByEmail(String email);
    List<UserResponse> findByRoleId(Integer roleId);
    List<UserResponse> findByGymId(Integer gymId);
    List<UserResponse> findByStatus(Integer status);
    UserResponse create(UserRequest request);
    UserResponse update(Integer id, UserRequest request);
    void delete(Integer id);
    boolean existsById(Integer id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}