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

    @JsonProperty("Product name")
    private String name;

    @JsonProperty("Price")
    private BigDecimal price;

    @JsonProperty("Stock")
    private Integer stock;

    @JsonProperty("Status")
    private Integer status;

    @JsonProperty("Photo")
    private String photo;
}