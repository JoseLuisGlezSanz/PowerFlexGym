package com.example.proyecto.mapper;

import com.example.proyecto.dto.CustomerResponse;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.dto.VisitRequest;
import com.example.proyecto.dto.VisitResponse;
import com.example.proyecto.model.Visit;

public class VisitMapper {
    public static VisitResponse toResponse(Visit visit) {
        if (visit == null)
            return null;

        GymResponse gymResponse = GymResponse.builder()
                .idGym(visit.getGym().getIdGym())
                .gym(visit.getGym().getGym())
                .build();

        CustomerResponse customerResponse = CustomerMapper.toResponse(visit.getCustomer());

        return VisitResponse.builder()
                .idVisit(visit.getIdVisit())
                .idCustomer(visit.getCustomer().getIdCustomer())
                .date(visit.getDate())
                .pending(visit.getPending())
                .gym(gymResponse)
                .customer(customerResponse)
                .build();
    }

    public static Visit toEntity(VisitRequest dto) {
        if (dto == null)
            return null;
        return Visit.builder()
                .date(dto.getDate())
                .pending(dto.getPending())
                .build();
    }

    public static void copyToEntity(VisitRequest dto, Visit entity) {
        if (dto == null || entity == null)
            return;
        entity.setDate(dto.getDate());
        entity.setPending(dto.getPending());
    }
}
