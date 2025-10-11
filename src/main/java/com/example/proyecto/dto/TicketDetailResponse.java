package com.example.proyecto.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TicketDetailResponse {
    private Integer idDatailTicket;
    private Integer amount;
    private Double unitPrice;
    private Double subtotal;
    private ProductResponse product;
    private Integer idTicket;
}