package com.example.proyecto.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RoleResponse {
    private Integer idRole;
    private String role;
    private Integer status;
}