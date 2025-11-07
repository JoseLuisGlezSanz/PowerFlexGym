package com.example.proyecto.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDetailResponse {
    @JsonProperty("Detail identifier")
    private Long id;

    private Integer amount;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private Long productId;
    private String productName;
    private Long ticketId;
}