package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.MembershipRequest;
import com.example.proyecto.dto.MembershipResponse;

public interface MembershipService {
    List<MembershipResponse> findAll();
    MembershipResponse findById(Integer id);
    MembershipResponse save(MembershipRequest membershipRequest);
    MembershipResponse update(Integer id, MembershipRequest membershipRequest);
    void delete(Integer id);
    List<MembershipResponse> findByMembershipName(String membershipName);
    List<MembershipResponse> findByStatus(Integer status);
    List<MembershipResponse> findByGymId(Integer idGym);
}