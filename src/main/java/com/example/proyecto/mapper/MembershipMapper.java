package com.example.proyecto.mapper;

import com.example.proyecto.dto.MembershipRequest;
import com.example.proyecto.dto.MembershipResponse;
import com.example.proyecto.model.Gym;
import com.example.proyecto.model.Membership;

public class MembershipMapper {
    public static MembershipResponse toResponse(Membership membership) {
        if (membership == null) return null;

        return MembershipResponse.builder()
                .id(membership.getId())
                .name(membership.getName())
                .duration(membership.getDuration())
                .price(membership.getPrice())
                .status(membership.getStatus())
                .gymId(membership.getGym().getId())
                .build();
    }

    public static Membership toEntity(MembershipRequest membershipRequest, Gym gym) {
        if (membershipRequest == null) return null;
        
        return Membership.builder()
                .name(membershipRequest.getName())
                .duration(membershipRequest.getDuration())
                .price(membershipRequest.getPrice())
                .status(membershipRequest.getStatus())
                .gym(gym)
                .build();
    }

    public static void copyToEntity(MembershipRequest membershipRequest, Membership entity, Gym gym) {
        if (membershipRequest == null || entity == null) return;
        
        entity.setName(membershipRequest.getName());
        entity.setDuration(membershipRequest.getDuration());
        entity.setPrice(membershipRequest.getPrice());
        entity.setStatus(membershipRequest.getStatus());
        entity.setGym(gym);
    }
}