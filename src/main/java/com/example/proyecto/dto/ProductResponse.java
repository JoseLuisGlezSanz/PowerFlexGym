package com.example.proyecto.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductResponse {
    private Integer idProduct;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Integer status;
    private String photo;
}