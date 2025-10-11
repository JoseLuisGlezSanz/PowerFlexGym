package com.example.proyecto.service;

import java.util.List;
import java.util.Optional;

import com.example.proyecto.dto.CustomerMembershipRequest;
import com.example.proyecto.dto.CustomerMembershipResponse;
import com.example.proyecto.model.CustomerMembershipId;


public interface CustomerMembershipService {
    List<CustomerMembershipResponse> findAll();
    Optional<CustomerMembershipResponse> findById(CustomerMembershipId id);
    List<CustomerMembershipResponse> findByCustomerId(Integer customerId);
    List<CustomerMembershipResponse> findByMembershipId(Integer membershipId);
    List<CustomerMembershipResponse> findByGymId(Integer gymId);
    List<CustomerMembershipResponse> findActiveMemberships();
    CustomerMembershipResponse create(CustomerMembershipRequest request);
    CustomerMembershipResponse update(CustomerMembershipId id, CustomerMembershipRequest request);
    void delete(CustomerMembershipId id);
    boolean existsById(CustomerMembershipId id);
    boolean isMembershipActive(Integer customerId, Integer membershipId);
}