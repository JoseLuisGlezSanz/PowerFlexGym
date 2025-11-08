package com.example.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipSaleResponse {
    @JsonProperty("Sale identifier")
    private Integer idMembershipSale;

    private LocalDateTime date;
    private Double payment;
    private Boolean cancellation;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long membershipId;
    private String membershipName;
    private Long customerId;
    private String customerName;
    private Long gymId;
    private String gymName;
    private Long userId;
    private String userName;
}