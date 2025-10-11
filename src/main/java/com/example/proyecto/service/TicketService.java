package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.TicketRequest;
import com.example.proyecto.dto.TicketResponse;

public interface TicketService {
    List<TicketResponse> findAll();
    TicketResponse findById(Integer id);
    TicketResponse create(TicketRequest request);
    TicketResponse update(Integer id, TicketRequest request);
    void delete(Integer id);
}