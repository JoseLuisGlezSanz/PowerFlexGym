package com.example.proyecto.dto;

import lombok.Data;

@Data
public class TicketDetailRequest {
    private Integer amount;
    private Long productId;
    private Long ticketId;
}