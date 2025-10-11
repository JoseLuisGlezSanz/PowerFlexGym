package com.example.proyecto.dto;

import lombok.Builder;
import lombok.Value;
import java.util.List;

@Value
@Builder
public class GymResponse {
    private Integer idGym;
    private String gym;
}