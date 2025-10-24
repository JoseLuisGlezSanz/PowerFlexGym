package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.GymRequest;
import com.example.proyecto.dto.GymResponse;

public interface GymService {
    List<GymResponse> findAll();
    GymResponse findById(Integer id);
    GymResponse save(GymRequest gymRequest);
    GymResponse update(Integer id, GymRequest gymRequest);
    List<GymResponse> findByGym(String gym);
    List<GymResponse> getAll(int page, int pageSize);
}