package com.example.proyecto.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.proyecto.dto.MembershipSaleRequest;
import com.example.proyecto.dto.MembershipSaleResponse;

public interface MembershipSaleService {
    List<MembershipSaleResponse> findAll();
    Optional<MembershipSaleResponse> findById(Integer id);
    List<MembershipSaleResponse> findByCustomerId(Integer customerId);
    List<MembershipSaleResponse> findByUserId(Integer userId);
    List<MembershipSaleResponse> findByGymId(Integer gymId);
    List<MembershipSaleResponse> findByMembershipId(Integer membershipId);
    List<MembershipSaleResponse> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<MembershipSaleResponse> findActiveSales();
    MembershipSaleResponse create(MembershipSaleRequest request);
    MembershipSaleResponse update(Integer id, MembershipSaleRequest request);
    void delete(Integer id);
    boolean existsById(Integer id);
    Double getTotalSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    MembershipSaleResponse cancelSale(Integer id);
}