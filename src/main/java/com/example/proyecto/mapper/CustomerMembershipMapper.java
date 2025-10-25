package com.example.proyecto.mapper;

import com.example.proyecto.dto.CustomerMembershipRequest;
import com.example.proyecto.dto.CustomerMembershipResponse;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.model.CustomerMembership;

public class CustomerMembershipMapper {
    public static CustomerMembershipResponse toResponse(CustomerMembership customerMembership) {
        if (customerMembership == null) return null;
        
        GymResponse gymResponse = GymResponse.builder()
            .idGym(customerMembership.getGym().getIdGym())
            .gym(customerMembership.getGym().getGym())
            .build();

        return CustomerMembershipResponse.builder()
                .idCustomer(customerMembership.getCustomer().getIdCustomer())
                .customerName(customerMembership.getCustomer().getName())
                .idMembership(customerMembership.getMembership().getIdMembership())
                .membershipName(customerMembership.getMembership().getMembership())
                .startDate(customerMembership.getStartDate())
                .endDate(customerMembership.getEndDate())
                .memberSince(customerMembership.getMemberSince())
                .membershipStatus(customerMembership.getMembershipStatus())
                .gym(gymResponse)
                .build();
    }

    public static CustomerMembership toEntity(CustomerMembershipRequest dto) {
        if (dto == null) return null;

        return CustomerMembership.builder()
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .memberSince(dto.getMemberSince())
                .membershipStatus(dto.getMembershipStatus())
                .build();
    }

    public static void copyToEntity(CustomerMembershipRequest dto, CustomerMembership entity) {
        if (dto == null || entity == null) return;
        
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setMemberSince(dto.getMemberSince());
        entity.setMembershipStatus(dto.getMembershipStatus());
    }
}