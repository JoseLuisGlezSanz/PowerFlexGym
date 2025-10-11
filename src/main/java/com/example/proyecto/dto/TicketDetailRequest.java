package com.example.proyecto.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TicketDetailRequest {
    @NotNull private Integer amount;
    @NotNull private BigDecimal unitPrice;
    @NotNull private BigDecimal subtotal;
    @NotNull private Integer idProduct;
    @NotNull private Integer idTicket;
}