package com.example.proyecto.mapper;

import java.util.Iterator;
import java.util.Set;

import com.example.proyecto.dto.UserLoginRequest;
import com.example.proyecto.dto.UserLoginResponse;
import com.example.proyecto.model.Role;
import com.example.proyecto.model.User;

public final class UserLoginMapper {
    public static UserLoginResponse toResponse(User user) {
        if (user == null) return null;

        Set<Role> roles = (Set<Role>) user.getRole();
        Role firstElement = new Role();
        if (!roles.isEmpty()) {
            Iterator<Role> iterator = roles.iterator();
            firstElement = iterator.next();
        }

        return UserLoginResponse.builder()
                .mail(user.getMail())
                .roleName(firstElement.getAuthority())
                .build();
    }

    public static User toEntity(UserLoginRequest dto) {
        if (dto == null) return null;

        User user = User.builder()
                .mail(dto.getMail())
                .password(dto.getPassword())
                .build();
        return user;
    }

    public static void copyToEntity(UserLoginRequest dto, User entity) {
        if (dto == null || entity == null) return;
        entity.setMail(dto.getMail());
        entity.setPassword(dto.getPassword());
    }
}