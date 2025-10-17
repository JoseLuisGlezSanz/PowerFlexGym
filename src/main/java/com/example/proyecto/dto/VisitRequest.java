package com.example.proyecto.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VisitRequest {
    private Integer idCustomer;
    private LocalDateTime date;
    private Integer pending;
    private Integer idGym;
}