package com.example.proyecto.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.proyecto.dto.TicketRequest;
import com.example.proyecto.dto.TicketResponse;

public interface TicketService {
    List<TicketResponse> findAll();
    Optional<TicketResponse> findById(Integer id);
    List<TicketResponse> findByUserId(Integer userId);
    List<TicketResponse> findByCustomerId(Integer customerId);
    List<TicketResponse> findByStatus(Integer status);
    List<TicketResponse> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<TicketResponse> findByPaymentMethod(Integer methodPayment);
    TicketResponse create(TicketRequest request);
    TicketResponse update(Integer id, TicketRequest request);
    void delete(Integer id);
    boolean existsById(Integer id);
    Double getTotalSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}