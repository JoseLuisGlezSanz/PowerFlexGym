package com.example.proyecto.mapper;

import com.example.proyecto.dto.VisitRequest;
import com.example.proyecto.dto.VisitResponse;
import com.example.proyecto.model.Visit;

public class VisitMapper {
    public static VisitResponse toResponse(Visit visit) {
        if (visit == null)
            return null;
        return VisitResponse.builder()
                .idVisit(visit.getIdVisit())
                .idCustomer(visit.getIdCustomer())
                .date(visit.getDate())
                .pending(visit.getPending())
                .idGym(visit.getIdGym())
                .build();
    }

    public static Visit toEntity(VisitRequest dto) {
        if (dto == null)
            return null;
        return Visit.builder()
                .idCustomer(dto.getIdCustomer())
                .date(dto.getDate())
                .pending(dto.getPending())
                .idGym(dto.getIdGym())
                .build();
    }

    public static void copyToEntity(VisitRequest dto, Visit entity) {
        if (dto == null || entity == null)
            return;
        entity.setIdCustomer(dto.getIdCustomer());
        entity.setDate(dto.getDate());
        entity.setPending(dto.getPending());
        entity.setIdGym(dto.getIdGym());
    }
}
