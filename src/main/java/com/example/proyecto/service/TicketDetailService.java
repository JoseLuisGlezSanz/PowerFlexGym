package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.TicketDetailRequest;
import com.example.proyecto.dto.TicketDetailResponse;

public interface TicketDetailService {
    List<TicketDetailResponse> findAll();
    TicketDetailResponse findById(Integer id);
    TicketDetailResponse save(TicketDetailRequest ticketDetailRequest);
    TicketDetailResponse update(Integer id, TicketDetailRequest ticketDetailRequest);
    // void delete(Integer id);
    List<TicketDetailResponse> findByTicketId(Integer idTicket);
    // List<TicketDetailResponse> findByProductId(Integer idProduct);
    List<TicketDetailResponse> getAll(int page, int pageSize);
}