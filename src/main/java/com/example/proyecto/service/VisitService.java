package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.VisitRequest;
import com.example.proyecto.dto.VisitResponse;

public interface VisitService {
    List<VisitResponse> findAll();
    VisitResponse findById(Long id);
    VisitResponse create(VisitRequest visitRequest);
    VisitResponse update(Long id, VisitRequest visitRequest);
    // void delete(Long id);
    List<VisitResponse> findByCustomerId(Long customerId);
    List<VisitResponse> findByGymId(Long gymId);
}