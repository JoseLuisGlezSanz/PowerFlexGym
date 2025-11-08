package com.example.proyecto.mapper;

import java.time.LocalDate;

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
                .customerId(customerMembership.getCustomer().getId())
                .customerName(customerMembership.getCustomer().getName())
                .membershipId(customerMembership.getMembership().getId())
                .membershipName(customerMembership.getMembership().getName())
                .startDate(customerMembership.getStartDate())
                .endDate(customerMembership.getEndDate())
                .memberSince(customerMembership.getMemberSince())
                .membershipStatus(customerMembership.getMembershipStatus())
                .gymId(customerMembership.getGym().getId())
                .build();
    }

    public static CustomerMembership toEntity(CustomerMembershipRequest customerMembershipRequest, Customer customer, Membership membership, Gym gym) {
        if (customerMembershipRequest == null) return null;

        return CustomerMembership.builder()
                .startDate(LocalDate.now())
                .endDate(customerMembershipRequest.getEndDate())
                .memberSince(customerMembershipRequest.getMemberSince())
                .membershipStatus(customerMembershipRequest.getMembershipStatus())
                .customer(customer)
                .membership(membership)
                .gym(gym)
                .build();
    }

    public static void copyToEntity(CustomerMembershipRequest customerMembershipRequest, CustomerMembership entity, Customer customer, Membership membership, Gym gym) {
        if (customerMembershipRequest == null || entity == null) return;
        
        entity.setEndDate(customerMembershipRequest.getEndDate());
        entity.setMemberSince(customerMembershipRequest.getMemberSince());
        entity.setMembershipStatus(customerMembershipRequest.getMembershipStatus());
        entity.setCustomer(customer);
        entity.setMembership(membership);
        entity.setGym(gym);
    }
}