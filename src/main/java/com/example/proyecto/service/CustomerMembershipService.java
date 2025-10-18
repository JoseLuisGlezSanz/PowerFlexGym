package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.CustomerMembershipRequest;
import com.example.proyecto.dto.CustomerMembershipResponse;


public interface CustomerMembershipService {
    List<CustomerMembershipResponse> findAll();
    CustomerMembershipResponse findById(Integer idCustomer, Integer idMembership);
    CustomerMembershipResponse save(CustomerMembershipRequest customerMembershipRequest);
    CustomerMembershipResponse update(Integer idCustomer, Integer idMembership, CustomerMembershipRequest req);
    void delete(Integer idCustomer, Integer idMembership);
    List<CustomerMembershipResponse> findByCustomerId(Integer idCustomer);
    List<CustomerMembershipResponse> findByMembershipStatus(Boolean status);
    List<CustomerMembershipResponse> findByCustomerIdCustomerAndMembershipStatus(Integer idCustomer, Boolean status);
    List<CustomerMembershipResponse> findActiveMembershipsExpiringSoon();
}