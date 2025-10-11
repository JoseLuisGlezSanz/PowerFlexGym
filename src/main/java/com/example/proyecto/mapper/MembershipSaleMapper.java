package com.example.proyecto.mapper;

import com.example.proyecto.dto.MembershipSaleRequest;
import com.example.proyecto.dto.MembershipSaleResponse;
import com.example.proyecto.model.MembershipSale;

public class MembershipSaleMapper {
    public static MembershipSaleResponse toResponse(MembershipSale membershipSale) {
        if (membershipSale == null)
            return null;
        return MembershipSaleResponse.builder()
                .idMembershipSale(membershipSale.getIdMembershipSales())
                .date(membershipSale.getDate())
                .payment(membershipSale.getPayment())
                .cancellation(membershipSale.getCancellation())
                .startDate(membershipSale.getStartDate())
                .endDate(membershipSale.getEndDate())
                .idMembership(membershipSale.getMembership().getIdMembership())
                .idCustomer(membershipSale.getCustomer().getIdCustomer())
                .idGym(membershipSale.getGym().getIdGym())
                .idUser(membershipSale.getUser().getIdUser())
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
        entity.setCancellation(dto.getCancellation());
    }
}
