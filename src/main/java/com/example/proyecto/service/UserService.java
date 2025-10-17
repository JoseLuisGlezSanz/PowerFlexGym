package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.UserRequest;
import com.example.proyecto.dto.UserResponse;

public interface UserService {
    List<UserResponse> findAll();
    UserResponse findById(Integer id);
    UserResponse save(UserRequest userRequest);
    UserResponse update(Integer id, UserRequest userRequest);
    void delete(Integer id);
    UserResponse findByMail(String mail);
    UserResponse findByUsername(String username);
    List<UserResponse> findByRoleId(Integer idRole);
}