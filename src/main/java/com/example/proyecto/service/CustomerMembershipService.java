package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.CustomerMembershipRequest;
import com.example.proyecto.dto.CustomerMembershipResponse;


public interface CustomerMembershipService {
    List<CustomerMembershipResponse> findAll();
    CustomerMembershipResponse findById(Long customerId, Long membershipId);
    CustomerMembershipResponse create(CustomerMembershipRequest customerMembershipRequest);
    CustomerMembershipResponse update(Long customerId, Long membershipId, CustomerMembershipRequest customerMembershipRequest);
    // void delete(Integer idCustomer, Integer idMembership);
    List<CustomerMembershipResponse> findByCustomerId(Long customerId);
    List<CustomerMembershipResponse> findByMembershipId(Long membershipId);
    List<CustomerMembershipResponse> findByGymId(Long gymId);
}