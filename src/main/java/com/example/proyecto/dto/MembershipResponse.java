package com.example.proyecto.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MembershipResponse {
    private Integer idMembership;
    private String membership;
    private String duration;
    private Double price;
    private Integer status;
    private GymResponse gym;
}