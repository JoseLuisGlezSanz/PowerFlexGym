package com.example.proyecto.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CustomerMembershipRequest {
    private Integer idCustomer;
    private Integer idMembership;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean membershipStatus;
    private LocalDateTime memberSince;
    private Integer idGym;
}