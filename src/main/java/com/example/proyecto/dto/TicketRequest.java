package com.example.proyecto.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TicketRequest {
    private BigDecimal total;
    private Integer status;
    private Integer methodPayment;
    private BigDecimal paymentWith;
    private Long customerId;
    private Long userId;
}