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
public class ProductResponse {
    @JsonProperty("Product identifier")
    private Long id;

    private String name;
    private BigDecimal price;
    private Integer stock;
    private Integer status;
    private String photo;
}