package com.example.proyecto.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.proyecto.dto.VisitRequest;
import com.example.proyecto.dto.VisitResponse;

public interface VisitService {
    List<VisitResponse> findAll();
    Optional<VisitResponse> findById(Integer id);
    List<VisitResponse> findByCustomerId(Integer customerId);
    List<VisitResponse> findByGymId(Integer gymId);
    List<VisitResponse> findByPendingStatus(Integer pending);
    List<VisitResponse> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<VisitResponse> findPendingVisitsByGym(Integer gymId);
    VisitResponse create(VisitRequest request);
    VisitResponse update(Integer id, VisitRequest request);
    void delete(Integer id);
    boolean existsById(Integer id);
    Long countVisitsByCustomerAndDateRange(Integer customerId, LocalDateTime startDate, LocalDateTime endDate);
}