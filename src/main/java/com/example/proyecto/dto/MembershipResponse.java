package com.example.proyecto.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MembershipResponse {

    private Integer idMembership;

    private String membership;

    private String duration;

    private BigDecimal price;

    private Integer status;

    private Integer idGym;
}
