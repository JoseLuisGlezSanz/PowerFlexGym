package com.example.proyecto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmergencyContactResponse {
    @JsonProperty("Contact identifier:")
    private Integer idContact;
    private String contactName;
    private String contactPhone;
}