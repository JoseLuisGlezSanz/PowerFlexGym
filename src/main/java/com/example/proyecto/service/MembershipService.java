package com.example.proyecto.service;

import java.util.List;
import com.example.proyecto.dto.MembershipRequest;
import com.example.proyecto.dto.MembershipResponse;

public interface MembershipService {
    List<MembershipResponse> findAll();
    MembershipResponse findById(Integer id);
    MembershipResponse create(MembershipRequest request);
    MembershipResponse update(Integer id, MembershipRequest request);
    void delete(Integer id);
}
