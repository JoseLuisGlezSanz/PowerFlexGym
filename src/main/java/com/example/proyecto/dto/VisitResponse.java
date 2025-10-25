package com.example.proyecto.dto;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

@Value
@Builder
public class VisitResponse {
    @JsonProperty("Visit identifier")
    private Integer idVisit;

    @JsonProperty("Customer identifier")
    private Integer idCustomer;

    @JsonProperty("Customer name")
    private String customerName;

    @JsonProperty("Visit date")
    private LocalDateTime date;

    @JsonProperty("Pending visits")
    private Integer pending;

    @JsonProperty("Gym identifier")
    private Integer idGym;

    @JsonProperty("Gym name")
    private String gymName;
}