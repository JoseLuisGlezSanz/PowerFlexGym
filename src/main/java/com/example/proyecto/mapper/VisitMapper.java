package com.example.proyecto.mapper;

import com.example.proyecto.dto.VisitRequest;
import com.example.proyecto.dto.VisitResponse;
import com.example.proyecto.model.Visit;

public class VisitMapper {
    public static VisitResponse toResponse(Visit visit) {
        if (visit == null) return null;
        
        return VisitResponse.builder()
                .idVisit(visit.getIdVisit())
                .date(visit.getDate())
                .pending(visit.getPending())
                .idCustomer(visit.getCustomer().getIdCustomer())
                .customerName(visit.getCustomer().getName())
                .idGym(visit.getGym().getIdGym())
                .gymName(visit.getGym().getGym())
                .build();
    }

    public static Visit toEntity(VisitRequest dto) {
        if (dto == null) return null;
        
        return Visit.builder()
                .date(dto.getDate())
                .pending(dto.getPending())
                .build();
    }

    public static void copyToEntity(VisitRequest dto, Visit entity) {
        if (dto == null || entity == null) return;
        
        entity.setDate(dto.getDate());
        entity.setPending(dto.getPending());
    }
}