package com.example.proyecto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank private String user;
    @NotBlank private String mail;
    @NotBlank private String phone;
    @NotBlank private String name;
    @NotBlank private String password;
    @NotNull private Integer status;
    @NotNull private Integer idRole;
    @NotNull private Integer idGym;
}