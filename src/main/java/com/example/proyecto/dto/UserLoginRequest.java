package com.example.proyecto.dto;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String mail;
    private String password;
}
