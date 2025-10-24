package com.example.proyecto.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmergencyContactResponse {
    private Integer idContact;
    private String contactName;
    private String contactPhone;
}