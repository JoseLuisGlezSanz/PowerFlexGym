package com.example.proyecto.mapper;

import java.math.BigDecimal;

import com.example.proyecto.dto.TicketDetailRequest;
import com.example.proyecto.dto.TicketDetailResponse;
import com.example.proyecto.model.Product;
import com.example.proyecto.model.Ticket;
import com.example.proyecto.model.TicketDetail;

public class TicketDetailMapper {
    public static TicketDetailResponse toResponse(TicketDetail ticketDetail) {
        if (ticketDetail == null) return null;
        
        return TicketDetailResponse.builder()
                .id(ticketDetail.getId())
                .amount(ticketDetail.getAmount())
                .unitPrice(ticketDetail.getUnitPrice())
                .subtotal(ticketDetail.getSubtotal())
                .productId(ticketDetail.getProduct().getId())
                .productName(ticketDetail.getProduct().getName())
                .ticketId(ticketDetail.getTicket().getId())
                .build();
    }

    public static TicketDetail toEntity(TicketDetailRequest ticketDetailRequest, Product product, Ticket ticket) {
        if (ticketDetailRequest == null) return null;
        
        return TicketDetail.builder()
                .amount(ticketDetailRequest.getAmount())
                .unitPrice(product.getPrice())
                .subtotal(product.getPrice().multiply(BigDecimal.valueOf(ticketDetailRequest.getAmount())))
                .product(product)
                .ticket(ticket)
                .build();
    }

    public static void copyToEntity(TicketDetailRequest ticketDetailRequest, TicketDetail entity, Product product, Ticket ticket) {
        if (ticketDetailRequest == null || entity == null) return;
        
        entity.setAmount(ticketDetailRequest.getAmount());
        entity.setUnitPrice(product.getPrice());
        entity.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(ticketDetailRequest.getAmount())));
        entity.setProduct(product);
        entity.setTicket(ticket);
    }
}