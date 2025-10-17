package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.VisitRequest;
import com.example.proyecto.dto.VisitResponse;

public interface VisitService {
    List<VisitResponse> findAll();
    VisitResponse findById(Integer id);
    VisitResponse save(VisitRequest visitRequest);
    VisitResponse update(Integer id, VisitRequest visitRequest);
    void delete(Integer id);
    List<VisitResponse> findByCustomerId(Integer idCustomer);
    List<VisitResponse> findByGymId(Integer idGym);
    List<VisitResponse> findByPending(Integer pending);
}