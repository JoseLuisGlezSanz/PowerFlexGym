package com.example.proyecto.mapper;

import com.example.proyecto.dto.UserRequest;
import com.example.proyecto.dto.UserResponse;
import com.example.proyecto.model.Gym;
import com.example.proyecto.model.Role;
import com.example.proyecto.model.User;

public class UserMapper {
    public static UserResponse toResponse(User user) {
        if (user == null) return null;
            
        return UserResponse.builder()
                .id(user.getId())
                .nameUser(user.getNameUser())
                .mail(user.getMail())
                .phone(user.getPhone())
                .name(user.getName())
                .status(user.getStatus())
                .password(user.getPassword())
                .roleId(user.getRole().getId())
                .gymId(user.getGym().getId())
                .build();
    }

    public static User toEntity(UserRequest userRequest, Role role, Gym gym) {
        if (userRequest == null) return null;

        return User.builder()
                .nameUser(userRequest.getNameUser())
                .mail(userRequest.getMail())
                .phone(userRequest.getPhone())
                .name(userRequest.getName())
                .password(userRequest.getPassword())
                .status(userRequest.getStatus())
                .role(role)
                .gym(gym)
                .build();
    }

    public static void copyToEntity(UserRequest userRequest, User entity, Role role, Gym gym) {
        if (userRequest == null || entity == null) return;
        
        entity.setNameUser(userRequest.getNameUser());
        entity.setMail(userRequest.getMail());
        entity.setPhone(userRequest.getPhone());
        entity.setName(userRequest.getName());
        entity.setPassword(userRequest.getPassword());
        entity.setStatus(userRequest.getStatus());
        entity.setRole(role);
        entity.setGym(gym);
    }
}