package com.example.proyecto.dto;

import lombok.Data;

@Data
public class MembershipSaleRequest {
    private Double payment;
    private Long membershipId;
    private Long customerId;
    private Long gymId;
    private Long userId;
    private Boolean cancellation;
}