package com.example.proyecto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipResponse {
    @JsonProperty("Membership identifier:")
    private Long id;

    private String name;
    private String duration;
    private Double price;
    private Integer status;
    private Long gymId;
}