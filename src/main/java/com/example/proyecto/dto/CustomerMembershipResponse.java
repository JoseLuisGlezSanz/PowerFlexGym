package com.example.proyecto.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CustomerMembershipResponse {
    @JsonProperty("Customer identifier:")
    private Integer idCustomer;

    private String customerName;

    private Integer idMembership;

    private String membershipName;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime memberSince;

    private Boolean membershipStatus;

    private GymResponse gym;
}