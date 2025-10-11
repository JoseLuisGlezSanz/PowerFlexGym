package com.example.proyecto.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Integer status;
    private String photo;
}