package com.example.proyecto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmergencyContactResponse {
    @JsonProperty("Contact identifier:")
    private Integer idContact;

    @JsonProperty("Contact name:")
    private String contactName;

    @JsonProperty("Contact phone:")
    private String contactPhone;
}