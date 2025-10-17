package com.example.proyecto.dto;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
@Builder
public class MembershipSaleResponse {

    private Integer idMembershipSale;
    private LocalDateTime date;
    private Double payment;
    private Boolean cancellation;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer idMembership;
    private String membershipName;
    private Integer idCustomer;
    private String customerName;
    private Integer idGym;
    private String gymName;
    private Integer idUser;
    private String userName;
}
