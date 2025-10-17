package com.example.proyecto.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CustomerMembershipResponse {
    private Integer idCustomerMembership;
    private Integer idCustomer;
    private String customerName;
    private Integer idMembership;
    private String membershipName;
    private Integer idGym;
    private String gymName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime memberSince;
    private Boolean membershipStatus;
}