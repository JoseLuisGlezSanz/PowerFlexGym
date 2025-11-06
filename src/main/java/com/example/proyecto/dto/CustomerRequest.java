package com.example.proyecto.dto;

import lombok.Data;
import java.sql.Date;

@Data
public class CustomerRequest {
    private String name;
    private String cologne;
    private String phone;
    private Date birthDate;
    private Boolean medicalCondition;
    private String photo;
    private String photoCredential;
    private Boolean verifiedNumber;
    private Long gymId;
    private String contactName;
    private String contactPhone;
}