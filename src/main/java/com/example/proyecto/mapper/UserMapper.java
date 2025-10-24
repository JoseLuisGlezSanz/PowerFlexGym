package com.example.proyecto.mapper;

import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.dto.RoleResponse;
import com.example.proyecto.dto.UserRequest;
import com.example.proyecto.dto.UserResponse;
import com.example.proyecto.model.User;

public class UserMapper {
    public static UserResponse toResponse(User user) {
        if (user == null) return null;
        
        RoleResponse roleResponse = RoleResponse.builder()
            .idRole(user.getRole().getIdRole())
            .role(user.getRole().getRole())
            .status(user.getRole().getStatus())
            .build();

        GymResponse gymResponse = GymResponse.builder()
            .idGym(user.getGym().getIdGym())
            .gym(user.getGym().getGym())
            .build();
            
        return UserResponse.builder()
                .idUser(user.getIdUser())
                .user(user.getUser())
                .mail(user.getMail())
                .phone(user.getPhone())
                .name(user.getName())
                .status(user.getStatus())
                .password(user.getPassword())
                .role(roleResponse)
                .gym(gymResponse)
                .build();
    }

    public static User toEntity(UserRequest dto) {
        if (dto == null) return null;

        return User.builder()
                .user(dto.getUser())
                .mail(dto.getMail())
                .phone(dto.getPhone())
                .name(dto.getName())
                .password(dto.getPassword())
                .status(dto.getStatus())
                .build();
    }

    public static void copyToEntity(UserRequest dto, User entity) {
        if (dto == null || entity == null) return;
        
        entity.setUser(dto.getUser());
        entity.setMail(dto.getMail());
        entity.setPhone(dto.getPhone());
        entity.setName(dto.getName());
        entity.setPassword(dto.getPassword());
        entity.setStatus(dto.getStatus());
    }
}