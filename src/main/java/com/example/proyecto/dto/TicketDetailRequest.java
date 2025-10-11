package com.example.proyecto.dto;

import lombok.Data;

@Data
public class TicketDetailRequest {
    private Integer amount;
    private Double unitPrice;
    private Double subtotal;
    private Integer idProduct;
    private Integer idTicket;
}