package com.example.proyecto.dto;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class TicketResponse {
    private Integer idTicket;
    private LocalDateTime date;
    private BigDecimal total;
    private Integer status;
    private LocalDateTime saleDate;
    private Integer methodPayment;
    private BigDecimal paymentWith;
    private CustomerResponse customer;
    private UserResponse user;
    private List<TicketDetailResponse> details;
}