package com.example.proyecto.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GymResponse {
    private Integer idGym;
    private String gym;
}