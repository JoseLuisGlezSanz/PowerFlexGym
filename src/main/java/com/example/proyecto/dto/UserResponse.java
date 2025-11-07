package com.example.proyecto.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserResponse {
    @JsonProperty("User identifier")
    private Long id;

    private String nameUser;
    private String mail;
    private String phone;
    private String name;
    private String password;
    private Integer status;
    private Long roleId;
    private Long gymId;
}