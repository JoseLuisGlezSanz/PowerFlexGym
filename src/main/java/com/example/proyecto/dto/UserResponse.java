package com.example.proyecto.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserResponse {
    @JsonProperty("User identifier")
    private Integer idUser;

    @JsonProperty("Username")
    private String user;

    @JsonProperty("Email")
    private String mail;

    @JsonProperty("Phone number")
    private String phone;

    @JsonProperty("Full name")
    private String name;

    @JsonProperty("Password")
    private String password;

    @JsonProperty("Status")
    private Integer status;

    @JsonProperty("Role information")
    private RoleResponse role;

    @JsonProperty("Gym information")
    private GymResponse gym;
}