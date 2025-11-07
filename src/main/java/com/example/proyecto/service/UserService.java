package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.UserRequest;
import com.example.proyecto.dto.UserResponse;

public interface UserService {
    List<UserResponse> findAll();
    UserResponse findById(Long id);
    UserResponse create(UserRequest userRequest);
    UserResponse update(Long id, UserRequest userRequest);
    // void delete(Long id);
    UserResponse findByMail(String mail);
    UserResponse findByUsername(String nameUser);
    List<UserResponse> findByRoleId(Long idRole);
    List<UserResponse> getAll(int page, int pageSize);
    List<UserResponse> findByGymId(Long idGym);
}