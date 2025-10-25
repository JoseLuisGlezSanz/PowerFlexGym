package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.MembershipSaleRequest;
import com.example.proyecto.dto.MembershipSaleResponse;

public interface MembershipSaleService {
    List<MembershipSaleResponse> findAll();
    MembershipSaleResponse findById(Integer id);
    MembershipSaleResponse save(MembershipSaleRequest membershipSaleRequest);
    MembershipSaleResponse update(Integer id, MembershipSaleRequest membershipSaleRequest);
    // void delete(Integer id);
    List<MembershipSaleResponse> findByUserId(Integer idUser);
    List<MembershipSaleResponse> findByCustomerId(Integer idCustomer);
    List<MembershipSaleResponse> findByGymId(Integer idGym);
    List<MembershipSaleResponse> getAll(int page, int pageSize);
    List<MembershipSaleResponse> findByUserId(int page, int pageSize, Integer idUser);
    List<MembershipSaleResponse> findByGymId(int page, int pageSize, Integer idGym);
}