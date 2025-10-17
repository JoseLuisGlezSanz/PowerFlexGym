package com.example.proyecto.mapper;

import com.example.proyecto.dto.TicketRequest;
import com.example.proyecto.dto.TicketResponse;
import com.example.proyecto.model.Ticket;

public class TicketMapper {
    public static TicketResponse toResponse(Ticket ticket) {
        if (ticket == null) return null;
        
        return TicketResponse.builder()
                .idTicket(ticket.getIdTicket())
                .date(ticket.getDate())
                .total(ticket.getTotal())
                .status(ticket.getStatus())
                .saleDate(ticket.getSaleDate())
                .methodPayment(ticket.getMethodPayment())
                .paymentWith(ticket.getPaymentWith())
                .idCustomer(ticket.getCustomer().getIdCustomer())
                .customerName(ticket.getCustomer().getName())
                .idUser(ticket.getUser().getIdUser())
                .userName(ticket.getUser().getName())
                .build();
    }

    public static Ticket toEntity(TicketRequest dto) {
        if (dto == null) return null;
        
        return Ticket.builder()
                .date(dto.getDate())
                .total(dto.getTotal())
                .status(dto.getStatus())
                .saleDate(dto.getSaleDate())
                .methodPayment(dto.getMethodPayment())
                .paymentWith(dto.getPaymentWith())
                .build();
    }

    public static void copyToEntity(TicketRequest dto, Ticket entity) {
        if (dto == null || entity == null) return;
        
        entity.setDate(dto.getDate());
        entity.setTotal(dto.getTotal());
        entity.setStatus(dto.getStatus());
        entity.setSaleDate(dto.getSaleDate());
        entity.setMethodPayment(dto.getMethodPayment());
        entity.setPaymentWith(dto.getPaymentWith());
    }
}
