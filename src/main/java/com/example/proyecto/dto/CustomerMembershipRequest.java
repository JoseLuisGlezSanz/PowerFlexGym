package com.example.proyecto.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CustomerMembershipRequest {
    private Long customerId;
    private Long membershipId;
    private Boolean membershipStatus;
    private LocalDate endDate;
    private LocalDateTime memberSince;
}