package com.example.proyecto.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TicketRequest {
    @NotNull private LocalDateTime date;
    @NotNull private BigDecimal total;
    @NotNull private Integer status;
    private LocalDateTime saleDate;
    private Integer methodPayment;
    private BigDecimal paymentWith;
    @NotNull private Integer idCustomer;
    @NotNull private Integer idUser;
}