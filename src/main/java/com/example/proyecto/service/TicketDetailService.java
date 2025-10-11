package com.example.proyecto.service;

import java.util.List;
import java.util.Optional;

import com.example.proyecto.dto.TicketDetailRequest;
import com.example.proyecto.dto.TicketDetailResponse;

public interface TicketDetailService {
    List<TicketDetailResponse> findAll();
    Optional<TicketDetailResponse> findById(Integer id);
    List<TicketDetailResponse> findByTicketId(Integer ticketId);
    List<TicketDetailResponse> findByProductId(Integer productId);
    TicketDetailResponse create(TicketDetailRequest request);
    TicketDetailResponse update(Integer id, TicketDetailRequest request);
    void delete(Integer id);
    boolean existsById(Integer id);
    List<TicketDetailResponse> createMultiple(List<TicketDetailRequest> requests);
}