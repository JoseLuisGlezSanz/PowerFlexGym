package com.example.proyecto.service;

import java.util.List;
import java.util.Optional;

import com.example.proyecto.dto.MembershipRequest;
import com.example.proyecto.dto.MembershipResponse;

public interface MembershipService {
    List<MembershipResponse> findAll();
    Optional<MembershipResponse> findById(Integer id);
    List<MembershipResponse> findByGymId(Integer gymId);
    List<MembershipResponse> findByStatus(Integer status);
    List<MembershipResponse> findActiveMemberships();
    Optional<MembershipResponse> findByNameAndGym(String name, Integer gymId);
    MembershipResponse create(MembershipRequest request);
    MembershipResponse update(Integer id, MembershipRequest request);
    void delete(Integer id);
    boolean existsById(Integer id);
}