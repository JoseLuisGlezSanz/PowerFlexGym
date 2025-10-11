package com.example.proyecto.dto;

import lombok.Data;

@Data
public class MembershipRequest {
    private String membership;
    private String duration;
    private Double price;
    private Integer status;
    private Integer idGym;
}