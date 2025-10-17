package com.example.proyecto.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserResponse {
    private Integer idUser;
    private String user;
    private String mail;
    private String phone;
    private String name;
    private Integer status;
    private Integer idRole;
    private String roleName;
    private Integer idGym;
    private String gymName;
}