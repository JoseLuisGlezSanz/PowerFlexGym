package com.example.proyecto.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TicketRequest {
    private LocalDateTime date;
    private BigDecimal total;
    private Integer status;
    private LocalDateTime saleDate;
    private Integer methodPayment;
    private BigDecimal paymentWith;
    private Integer idCustomer;
    private Integer idUser;
}