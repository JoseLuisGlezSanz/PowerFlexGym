package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.TicketRequest;
import com.example.proyecto.dto.TicketResponse;

public interface TicketService {
    List<TicketResponse> findAll();
    TicketResponse findById(Long id);
    TicketResponse create(TicketRequest ticketRequest);
    TicketResponse update(Long id, TicketRequest ticketRequest);
    // void delete(Integer id);
    List<TicketResponse> getAll(int page, int pageSize);
    List<TicketResponse> findAllTicketsByCustomerId(Long customerId);
    List<TicketResponse> findAllTicketsByUserId(Long userId);
    List<TicketResponse> getAllTicketsByCustomerId(int page, int pageSize, Long customerId);
    List<TicketResponse> getAllTicketsByUserId(int page, int pageSize, Long userId);
}