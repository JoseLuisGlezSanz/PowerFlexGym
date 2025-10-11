package com.example.proyecto.dto;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class CustomerResponse {
    private Integer idCustomer;
    private String name;
    private String cologne;
    private String phone;
    private LocalDate birthDate;
    private Boolean medicalCondition;
    private LocalDateTime registrationDate;
    private String photo;
    private String photoCredential;
    private Boolean verifiedNumber;
    private GymResponse gym;
    private EmergencyContactResponse emergencyContact;
    private List<CustomerMembershipResponse> memberships;
}