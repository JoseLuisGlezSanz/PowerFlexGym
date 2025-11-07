package com.example.proyecto.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String nameUser;
    private String mail;
    private String phone;
    private String name;
    private String password;
    private Integer status;
    private Long roleId;
    private Long gymId;
}