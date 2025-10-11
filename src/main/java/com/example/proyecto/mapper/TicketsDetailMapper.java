package com.example.proyecto.mapper;

import com.example.proyecto.dto.TicketDetailRequest;
import com.example.proyecto.dto.TicketDetailResponse;
import com.example.proyecto.model.TicketDetail;

public class TicketsDetailMapper {
    public static TicketDetailResponse toResponse(TicketDetail detail) {
        if (detail == null)
            return null;

        return TicketDetailResponse.builder()
                .idDatailTicket(detail.getIdDatailTicket())
                .amount(detail.getAmount())
                .unitPrice(detail.getUnitPrice())
                .subtotal(detail.getSubtotal())
                .product(ProductMapper.toResponse(detail.getProduct()))
                .build();
    }

    public static TicketDetail toEntity(TicketDetailRequest dto) {
        if (dto == null)
            return null;

        return TicketDetail.builder()
                .amount(dto.getAmount())
                .unitPrice(dto.getUnitPrice())
                .subtotal(dto.getSubtotal())
                .idProduct(dto.getIdProduct())
                .idTicket(dto.getIdTicket())
                .build();
    }

    public static void copyToEntity(TicketDetailRequest dto, TicketDetail entity) {
        if (dto == null || entity == null)
            return;
        entity.setAmount(dto.getAmount());
        entity.setUnitPrice(dto.getUnitPrice());
        entity.setSubtotal(dto.getSubtotal());
        entity.setIdProduct(dto.getIdProduct());
        entity.setIdTicket(dto.getIdTicket());
    }
}
