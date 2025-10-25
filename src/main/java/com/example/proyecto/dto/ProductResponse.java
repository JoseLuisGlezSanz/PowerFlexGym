package com.example.proyecto.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductResponse {
    @JsonProperty("Product identifier")
    private Integer idProduct;

    private String name;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

    private String photo;
}