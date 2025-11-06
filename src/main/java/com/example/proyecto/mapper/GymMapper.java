package com.example.proyecto.mapper;

import com.example.proyecto.dto.GymRequest;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.model.Gym;

public class GymMapper {
    public static GymResponse toResponse(Gym gym) {
        if (gym == null) return null;
        
        return GymResponse.builder()
                .id(gym.getId())
                .name(gym.getName())
                .build();
    }

    public static Gym toEntity(GymRequest gymRequest) {
        if (gymRequest == null) return null;
        
        return Gym.builder()
                .name(gymRequest.getName())
                .build();
    }

    public static void copyToEntity(GymRequest gymRequest, Gym entity) {
        if (gymRequest == null || entity == null) return;
        
        entity.setName(gymRequest.getName());
    }
}