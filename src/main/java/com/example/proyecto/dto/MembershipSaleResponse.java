package com.example.proyecto.dto;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

@Value
@Builder
public class MembershipSaleResponse {
    @JsonProperty("Sale identifier")
    private Integer idMembershipSale;

    @JsonProperty("Sale date")
    private LocalDateTime date;

    @JsonProperty("Payment amount")
    private Double payment;

    @JsonProperty("Cancellation status")
    private Boolean cancellation;

    @JsonProperty("Membership start date")
    private LocalDate startDate;

    @JsonProperty("Membership end date")
    private LocalDate endDate;

    @JsonProperty("Membership identifier")
    private Integer idMembership;

    @JsonProperty("Membership name")
    private String membershipName;

    @JsonProperty("Customer identifier")
    private Integer idCustomer;

    @JsonProperty("Customer name")
    private String customerName;

    @JsonProperty("Gym identifier")
    private Integer idGym;

    @JsonProperty("Gym name")
    private String gymName;

    @JsonProperty("User identifier")
    private Integer idUser;

    @JsonProperty("User name")
    private String userName;
}
