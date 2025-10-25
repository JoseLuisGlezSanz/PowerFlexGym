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

    private Integer amount;

    private Double unitPrice;

    private BigDecimal subtotal;

    private Integer idProduct;

    private String productName;

    private Integer idTicket;
}