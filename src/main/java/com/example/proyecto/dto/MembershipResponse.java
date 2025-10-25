package com.example.proyecto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MembershipResponse {
    @JsonProperty("Membership identifier:")
    private Integer idMembership;

    private String membership;

    private String duration;

    private Double price;

    private Integer status;

    private GymResponse gym;
}