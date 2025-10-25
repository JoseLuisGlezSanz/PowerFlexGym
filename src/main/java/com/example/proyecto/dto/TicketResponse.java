package com.example.proyecto.dto;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

@Value
@Builder
public class TicketResponse {
    @JsonProperty("Ticket identifier")
    private Integer idTicket;

    private LocalDateTime date;

    private BigDecimal total;

    private Integer status;

    private LocalDateTime saleDate;

    private Integer methodPayment;

    private BigDecimal paymentWith;

    private Integer idCustomer;

    private String customerName;

    private Integer idUser;

    private String userName;
}