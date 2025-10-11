package com.example.proyecto.service;

import java.util.List;
import java.util.Optional;

import com.example.proyecto.dto.GymRequest;
import com.example.proyecto.dto.GymResponse;

public interface GymService {
    List<GymResponse> findAll();
    Optional<GymResponse> findById(Integer id);
    GymResponse create(GymRequest request);
    GymResponse update(Integer id, GymRequest request);
    void delete(Integer id);
    boolean existsById(Integer id);
}