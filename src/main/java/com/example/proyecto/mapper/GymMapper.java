package com.example.proyecto.mapper;

import com.example.proyecto.dto.GymRequest;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.model.Gym;

public class GymMapper {
        public static GymResponse toResponse(Gym gym) {
        if (gym == null)
            return null;
        return GymResponse.builder()
                .idGym(gym.getIdGym())
                .gym(gym.getGym())
                .build();
    }

    public static Gym toEntity(GymRequest dto) {
        if (dto == null)
            return null;
        return Gym.builder()
                .gym(dto.getGym())
                .build();
    }

    public static void copyToEntity(GymRequest dto, Gym entity) {
        if (dto == null || entity == null)
            return;
        entity.setGym(dto.getGym());
    }
}