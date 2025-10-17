package com.example.proyecto.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TicketDetailRequest {
    private Integer amount;
    private Double unitPrice;
    private BigDecimal subtotal;
    private Integer idProduct;
    private Integer idTicket;
}