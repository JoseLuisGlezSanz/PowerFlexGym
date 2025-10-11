package com.example.proyecto.mapper;

import com.example.proyecto.dto.RoleRequest;
import com.example.proyecto.dto.RoleResponse;
import com.example.proyecto.model.Role;

public class RoleMapper {
    public static RoleResponse toResponse(Role role) {
        if (role == null)
            return null;
        return RoleResponse.builder()
                .idRole(role.getIdRole())
                .role(role.getRole())
                .status(role.getStatus())
                .build();
    }

    public static Role toEntity(RoleRequest dto) {
        if (dto == null)
            return null;
        return Role.builder()
                .role(dto.getRole())
                .status(dto.getStatus())
                .build();
    }

    public static void copyToEntity(RoleRequest dto, Role entity) {
        if (dto == null || entity == null)
            return;
        entity.setRole(dto.getRole());
        entity.setStatus(dto.getStatus());
    }
}
