package com.example.proyecto.dto;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;

@Value
@Builder
public class TicketDetailResponse {
    private Integer idDatailTicket;
    private Integer amount;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private ProductResponse product;
}