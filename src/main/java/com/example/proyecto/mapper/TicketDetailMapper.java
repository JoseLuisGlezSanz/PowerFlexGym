package com.example.proyecto.mapper;

import com.example.proyecto.dto.TicketDetailRequest;
import com.example.proyecto.dto.TicketDetailResponse;
import com.example.proyecto.model.TicketDetail;

public class TicketDetailMapper {
    public static TicketDetailResponse toResponse(TicketDetail ticketDetail) {
        if (ticketDetail == null) return null;
        
        return TicketDetailResponse.builder()
                .idDetailTicket(ticketDetail.getIdDetailTicket())
                .amount(ticketDetail.getAmount())
                .unitPrice(ticketDetail.getUnitPrice())
                .subtotal(ticketDetail.getSubtotal())
                .idProduct(ticketDetail.getProduct().getIdProduct())
                .productName(ticketDetail.getProduct().getName())
                .idTicket(ticketDetail.getTicket().getIdTicket())
                .build();
    }

    public static TicketDetail toEntity(TicketDetailRequest dto) {
        if (dto == null) return null;
        
        return TicketDetail.builder()
                .amount(dto.getAmount())
                .unitPrice(dto.getUnitPrice())
                .subtotal(dto.getSubtotal())
                .build();
    }

    public static void copyToEntity(TicketDetailRequest dto, TicketDetail entity) {
        if (dto == null || entity == null) return;
        
        entity.setAmount(dto.getAmount());
        entity.setUnitPrice(dto.getUnitPrice());
        entity.setSubtotal(dto.getSubtotal());
    }
}
