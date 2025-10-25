package com.example.proyecto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GymResponse {
    @JsonProperty("Gym identifier:")
    private Integer idGym;

    @JsonProperty("Gym name:")
    private String gym;
}