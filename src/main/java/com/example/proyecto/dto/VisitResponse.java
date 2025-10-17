package com.example.proyecto.dto;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder
public class VisitResponse {
    private Integer idVisit;
    private Integer idCustomer;
    private String customerName;
    private LocalDateTime date;
    private Integer pending;
    private Integer idGym;
    private String gymName;
}