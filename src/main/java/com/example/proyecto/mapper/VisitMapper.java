package com.example.proyecto.mapper;

import java.time.LocalDateTime;

import com.example.proyecto.dto.VisitRequest;
import com.example.proyecto.dto.VisitResponse;
import com.example.proyecto.model.Customer;
import com.example.proyecto.model.Gym;
import com.example.proyecto.model.Visit;

public class VisitMapper {
    public static VisitResponse toResponse(Visit visit) {
        if (visit == null) return null;
        
        return VisitResponse.builder()
                .id(visit.getId())
                .date(visit.getDate())
                .pending(visit.getPending())
                .customerId(visit.getCustomer().getId())
                .customerName(visit.getCustomer().getName())
                .gymId(visit.getGym().getId())
                .gymName(visit.getGym().getName())
                .build();
    }

    public static Visit toEntity(VisitRequest visitRequest, Customer customer, Gym gym) {
        if (visitRequest == null) return null;
        
        return Visit.builder()
                .date(LocalDateTime.now())
                .pending(visitRequest.getPending())
                .customer(customer)
                .gym(gym)
                .build();
    }

    public static void copyToEntity(VisitRequest visitRequest, Visit entity, Customer customer, Gym gym) {
        if (visitRequest == null || entity == null) return;
        
        entity.setPending(visitRequest.getPending());
        entity.setCustomer(customer);
        entity.setGym(gym);
    }
}