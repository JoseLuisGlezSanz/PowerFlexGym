package com.example.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    @JsonProperty("Customer identifier:")
    private Long id;

    private String name;
    private String cologne;
    private String phone;
    private Date birthDate;
    private Boolean medicalCondition;
    private String photo;
    private String photoCredential;
    private Boolean verifiedNumber;
    private Long gymId;
    private Long contactId;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime registrationDate;
}