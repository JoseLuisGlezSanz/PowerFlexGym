package com.example.proyecto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MembershipResponse {
    @JsonProperty("Membership identifier:")
    private Integer idMembership;

    @JsonProperty("Membership name:")
    private String membership;

    @JsonProperty("Duration:")
    private String duration;

    @JsonProperty("Price:")
    private Double price;

    @JsonProperty("Status:")
    private Integer status;

    @JsonProperty("Gym information:")
    private GymResponse gym;
}