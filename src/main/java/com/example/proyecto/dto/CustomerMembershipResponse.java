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

    @JsonProperty("Customer name:")
    private String customerName;

    @JsonProperty("Membership identifier:")
    private Integer idMembership;

    @JsonProperty("Membership name:")
    private String membershipName;

    @JsonProperty("Start date:")
    private LocalDate startDate;

    @JsonProperty("End date:")
    private LocalDate endDate;

    @JsonProperty("Member since:")
    private LocalDateTime memberSince;

    @JsonProperty("Membership status:")
    private Boolean membershipStatus;

    @JsonProperty("Gym information:")
    private GymResponse gym;
}