package com.example.proyecto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RoleResponse {
    @JsonProperty("Role identifier")
    private Long id;

    private String name;
    private Integer status;
}