package com.example.proyecto.mapper;

import com.example.proyecto.dto.CustomerMembershipRequest;
import com.example.proyecto.dto.CustomerMembershipResponse;
import com.example.proyecto.model.CustomerMembership;

public class CustomerMembershipMapper {
        public static CustomerMembershipResponse toResponse(CustomerMembership cm) {
        if (cm == null)
            return null;

        return CustomerMembershipResponse.builder()
                .idCustomer(cm.getCustomer().getIdCustomer()) 
                .idMembership(cm.getMembership().getIdMembership())
                .idGym(cm.getGym().getIdGym())
                .startDate(cm.getStartDate())
                .endDate(cm.getEndDate())
                .memberSince(cm.getMemberSince())
                .membershipStatus(cm.getMembershipStatus())
                .build();
    }

    public static CustomerMembership toEntity(CustomerMembershipRequest dto) {
        if (dto == null)
            return null;

        return CustomerMembership.builder()
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .membershipStatus(dto.getMembershipStatus())
                .build();
    }

    public static void copyToEntity(CustomerMembershipRequest dto, CustomerMembership entity) {
        if (dto == null || entity == null)
            return;
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setMembershipStatus(dto.getMembershipStatus());
    }
}
