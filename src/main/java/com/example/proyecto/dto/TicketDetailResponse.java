package com.example.proyecto.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TicketDetailResponse {
    private Integer idDetailTicket;
    private Integer amount;
    private Double unitPrice;
    private BigDecimal subtotal;
    private Integer idProduct;
    private String productName;
    private Integer idTicket;
}