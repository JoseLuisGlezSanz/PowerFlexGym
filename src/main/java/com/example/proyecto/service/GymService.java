package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.GymRequest;
import com.example.proyecto.dto.GymResponse;

public interface GymService {
    List<GymResponse> findAll();
    GymResponse findById(Long id);
    GymResponse create(GymRequest gymRequest);
    GymResponse update(Long id, GymRequest gymRequest);
    // void delete(Long id);
    GymResponse findByName(String gym);
    List<GymResponse> getAll(int page, int pageSize);
}