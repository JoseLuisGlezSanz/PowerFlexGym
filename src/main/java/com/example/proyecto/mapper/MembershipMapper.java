package com.example.proyecto.mapper;

import com.example.proyecto.dto.MembershipRequest;
import com.example.proyecto.dto.MembershipResponse;
import com.example.proyecto.model.Membership;

public class MembershipMapper {
    public static MembershipResponse toResponse(Membership membership) {
        if (membership == null) return null;
        
        return MembershipResponse.builder()
                .idMembership(membership.getIdMembership())
                .membership(membership.getMembership())
                .duration(membership.getDuration())
                .price(membership.getPrice())
                .status(membership.getStatus())
                .idGym(membership.getGym().getIdGym())
                .gymName(membership.getGym().getGym())
                .build();
    }

    public static Membership toEntity(MembershipRequest dto) {
        if (dto == null) return null;
        
        return Membership.builder()
                .membership(dto.getMembership())
                .duration(dto.getDuration())
                .price(dto.getPrice())
                .status(dto.getStatus())
                .build();
    }

    public static void copyToEntity(MembershipRequest dto, Membership entity) {
        if (dto == null || entity == null) return;
        
        entity.setMembership(dto.getMembership());
        entity.setDuration(dto.getDuration());
        entity.setPrice(dto.getPrice());
        entity.setStatus(dto.getStatus());
    }
}