package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.TicketRequest;
import com.example.proyecto.dto.TicketResponse;

public interface TicketService {
    List<TicketResponse> findAll();
    TicketResponse findById(Integer id);
    TicketResponse save(TicketRequest ticketRequest);
    TicketResponse update(Integer id, TicketRequest ticketRequest);
    void delete(Integer id);
    List<TicketResponse> findByCustomerId(Integer idCustomer);
    List<TicketResponse> findByUserId(Integer idUser);
}