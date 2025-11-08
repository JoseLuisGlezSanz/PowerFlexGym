package com.example.proyecto.dto;

import lombok.Data;

@Data
public class VisitRequest {
    private Long customerId;
    private Boolean pending;
    private Long gymId;
}