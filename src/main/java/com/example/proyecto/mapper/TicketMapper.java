package com.example.proyecto.mapper;

import java.time.LocalDateTime;

import com.example.proyecto.dto.TicketRequest;
import com.example.proyecto.dto.TicketResponse;
import com.example.proyecto.model.Customer;
import com.example.proyecto.model.Ticket;
import com.example.proyecto.model.User;

public class TicketMapper {
    public static TicketResponse toResponse(Ticket ticket) {
        if (ticket == null) return null;
        
        return TicketResponse.builder()
                .id(ticket.getId())
                .date(ticket.getDate())
                .total(ticket.getTotal())
                .status(ticket.getStatus())
                .saleDate(ticket.getSaleDate())
                .methodPayment(ticket.getMethodPayment())
                .paymentWith(ticket.getPaymentWith())
                .customerId(ticket.getCustomer().getId())
                .customerName(ticket.getCustomer().getName())
                .userId(ticket.getUser().getId())
                .userName(ticket.getUser().getName())
                .build();
    }

    public static Ticket toEntity(TicketRequest ticketRequest, Customer customer, User user) {
        if (ticketRequest == null) return null;
        
        return Ticket.builder()
                .date(LocalDateTime.now())
                .total(ticketRequest.getTotal())
                .status(ticketRequest.getStatus())
                .saleDate(LocalDateTime.now())
                .methodPayment(ticketRequest.getMethodPayment())
                .paymentWith(ticketRequest.getPaymentWith())
                .customer(customer)
                .user(user)
                .build();
    }

    public static void copyToEntity(TicketRequest ticketRequest, Ticket entity, Customer customer, User user) {
        if (ticketRequest == null || entity == null) return;
        
        entity.setTotal(ticketRequest.getTotal());
        entity.setStatus(ticketRequest.getStatus());
        entity.setMethodPayment(ticketRequest.getMethodPayment());
        entity.setPaymentWith(ticketRequest.getPaymentWith());
        entity.setCustomer(customer);
        entity.setUser(user);
    }
}