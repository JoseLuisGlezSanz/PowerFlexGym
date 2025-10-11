package com.example.proyecto.service;

import java.util.List;
import com.example.proyecto.dto.MembershipSaleRequest;
import com.example.proyecto.dto.MembershipSaleResponse;

public interface MembershipSaleService {
    List<MembershipSaleResponse> findAll();

    MembershipSaleResponse findById(Integer id);

    MembershipSaleResponse create(MembershipSaleRequest request);

    MembershipSaleResponse update(Integer id, MembershipSaleRequest request);
    
    void delete(Integer id);
}
