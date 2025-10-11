package com.example.proyecto.service;

import java.util.List;
import com.example.proyecto.dto.VisitRequest;
import com.example.proyecto.dto.VisitResponse;

public interface VisitService {
    List<VisitResponse> findAll();
    VisitResponse findById(Integer id);
    VisitResponse create(VisitRequest request);
    VisitResponse update(Integer id, VisitRequest request);
    void delete(Integer id);
}