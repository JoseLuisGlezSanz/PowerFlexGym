package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.TicketDetailRequest;
import com.example.proyecto.dto.TicketDetailResponse;

public interface TicketDetailService {
    List<TicketDetailResponse> findAll();
    TicketDetailResponse findById(Integer id);
    TicketDetailResponse create(TicketDetailRequest request);
    TicketDetailResponse update(Integer id, TicketDetailRequest request);
    void delete(Integer id);
}