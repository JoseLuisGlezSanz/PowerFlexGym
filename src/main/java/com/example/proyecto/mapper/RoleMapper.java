package com.example.proyecto.mapper;

import com.example.proyecto.dto.RoleRequest;
import com.example.proyecto.dto.RoleResponse;
import com.example.proyecto.model.Role;

public class RoleMapper {
    public static RoleResponse toResponse(Role role) {
        if (role == null) return null;
        
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .status(role.getStatus())
                .build();
    }

    public static Role toEntity(RoleRequest roleRequest) {
        if (roleRequest == null) return null;
        
        return Role.builder()
                .name(roleRequest.getName())
                .status(roleRequest.getStatus())
                .build();
    }

    public static void copyToEntity(RoleRequest roleRequest, Role entity) {
        if (roleRequest == null || entity == null) return;
        
        entity.setName(roleRequest.getName());
        entity.setStatus(roleRequest.getStatus());
    }
}
