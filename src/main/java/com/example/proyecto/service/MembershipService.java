package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.MembershipRequest;
import com.example.proyecto.dto.MembershipResponse;

public interface MembershipService {
    List<MembershipResponse> findAll();
    MembershipResponse findById(Long id);
    MembershipResponse create(MembershipRequest membershipRequest);
    MembershipResponse update(Long id, MembershipRequest membershipRequest);
    // void delete(Integer id);
    List<MembershipResponse> findMembershipByName(String name);
    List<MembershipResponse> findMembershipByGymId(Long gymId);
}