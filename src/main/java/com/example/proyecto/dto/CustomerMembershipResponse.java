package com.example.proyecto.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CustomerMembershipResponse {

    private Integer idCustomer;

    private Integer idMembership;

    private Integer idGym;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime memberSince;

    private Boolean membershipStatus;
}
