package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.UserRequest;
import com.example.proyecto.dto.UserResponse;

public interface UserService {
    List<UserResponse> findAll();
    UserResponse findById(Integer id);
    UserResponse create(UserRequest request);
    UserResponse update(Integer id, UserRequest request);
    void delete(Integer id);
}
