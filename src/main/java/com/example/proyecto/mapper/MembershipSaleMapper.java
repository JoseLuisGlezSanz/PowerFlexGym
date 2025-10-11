package com.example.proyecto.mapper;

import com.example.proyecto.dto.MembershipSaleRequest;
import com.example.proyecto.dto.MembershipSaleResponse;
import com.example.proyecto.model.MembershipSale;

public class MembershipSaleMapper {
    public static MembershipSaleResponse toResponse(MembershipSale membershipSale) {
        if (membershipSale == null)
            return null;
        return MembershipSaleResponse.builder()
                .idMembershipSale(membershipSale.getIdMembershipSale())
                .date(membershipSale.getDate())
                .payment(membershipSale.getPayment())
                .cancellation(membershipSale.getCancellation())
                .startDate(membershipSale.getStartDate())
                .endDate(membershipSale.getEndDate())
                .idMembership(membershipSale.getMembership())
                .idCustomer(membershipSale.getCustomer())
                .idGym(membershipSale.getGym())
                .idUser(membershipSale.getUser())
                .build();
    }

    public static MembershipSale toEntity(MembershipSaleRequest dto) {
        if (dto == null)
            return null;
        return MembershipSale.builder()
                .date(dto.getDate())
                .payment(dto.getPayment())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .idMembership(dto.getIdMembership())
                .idCustomer(dto.getIdCustomer())
                .idGym(dto.getIdGym())
                .idUser(dto.getIdUser())
                .cancellation(dto.getCancellation())
                .build();
    }

    public static void copyToEntity(MembershipSaleRequest dto, MembershipSale entity) {
        if (dto == null || entity == null)
            return;
        entity.setDate(dto.getDate());
        entity.setPayment(dto.getPayment());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setIdMembershipSales(dto.getIdMembership());
        entity.setIdCustomere(dto.getIdCustomer());
        entity.setIdGym(dto.getIdGym());
        entity.setIdUser(dto.getIdUser());
        entity.setCancellation(dto.getCancellation());
    }
}
