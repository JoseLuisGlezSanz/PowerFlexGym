package com.example.proyecto.mapper;

import java.time.LocalDateTime;

import com.example.proyecto.dto.CustomerMembershipRequest;
import com.example.proyecto.dto.CustomerMembershipResponse;
import com.example.proyecto.model.Customer;
import com.example.proyecto.model.CustomerMembership;
import com.example.proyecto.model.Gym;
import com.example.proyecto.model.Membership;

public class CustomerMembershipMapper {
    public static CustomerMembershipResponse toResponse(CustomerMembership customerMembership) {
        if (customerMembership == null) return null;
        
        return CustomerMembershipResponse.builder()
                .idCustomer(customerMembership.getCustomer().getIdCustomer())
                .customerName(customerMembership.getCustomer().getName())
                .idMembership(customerMembership.getMembership().getIdMembership())
                .membershipName(customerMembership.getMembership().getMembership())
                .startDate(customerMembership.getStartDate())
                .endDate(customerMembership.getEndDate())
                .memberSince(customerMembership.getMemberSince())
                .membershipStatus(customerMembership.getMembershipStatus())
                .idGym(customerMembership.getGym().getIdGym())
                .gymName(customerMembership.getGym().getGym())
                .build();
    }

    public static CustomerMembership toEntity(CustomerMembershipRequest dto) {
        if (dto == null) return null;
        
        Customer customer = new Customer();
        customer.setIdCustomer(dto.getIdCustomer());
        
        Membership membership = new Membership();
        membership.setIdMembership(dto.getIdMembership());

        Gym gym = new Gym();
        gym.setIdGym(dto.getIdGym());

        return CustomerMembership.builder()
                .customer(customer)
                .membership(membership)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .memberSince(dto.getMemberSince() != null ? 
                    dto.getMemberSince() : LocalDateTime.now())
                .membershipStatus(dto.getMembershipStatus() != null ? 
                    dto.getMembershipStatus() : true)
                .gym(gym)
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