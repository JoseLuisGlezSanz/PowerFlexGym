package com.example.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitResponse {
    @JsonProperty("Visit identifier")
    private Long id;

    private Long customerId;
    private String customerName;
    private LocalDateTime date;
    private Boolean pending;
    private Long gymId;
    private String gymName;
}