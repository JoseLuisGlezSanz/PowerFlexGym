package com.example.proyecto.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TicketDetailResponse {
    @JsonProperty("Detail identifier")
    private Integer idDetailTicket;

    @JsonProperty("Quantity")
    private Integer amount;

    @JsonProperty("Unit price")
    private Double unitPrice;

    @JsonProperty("Subtotal")
    private BigDecimal subtotal;

    @JsonProperty("Product identifier")
    private Integer idProduct;

    @JsonProperty("Product name")
    private String productName;

    @JsonProperty("Ticket identifier")
    private Integer idTicket;
}