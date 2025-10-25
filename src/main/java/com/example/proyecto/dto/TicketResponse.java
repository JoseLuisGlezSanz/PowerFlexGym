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

    @JsonProperty("Creation date")
    private LocalDateTime date;

    @JsonProperty("Total amount")
    private BigDecimal total;

    @JsonProperty("Status")
    private Integer status;

    @JsonProperty("Sale date")
    private LocalDateTime saleDate;

    @JsonProperty("Payment method")
    private Integer methodPayment;

    @JsonProperty("Amount paid")
    private BigDecimal paymentWith;

    @JsonProperty("Customer identifier")
    private Integer idCustomer;

    @JsonProperty("Customer name")
    private String customerName;

    @JsonProperty("User identifier")
    private Integer idUser;

    @JsonProperty("User name")
    private String userName;
}