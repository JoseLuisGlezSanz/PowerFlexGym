package com.example.proyecto.service;

import java.util.List;

import com.example.proyecto.dto.CustomerMembershipRequest;
import com.example.proyecto.dto.CustomerMembershipResponse;


public interface CustomerMembershipService {
    List<CustomerMembershipResponse> findAll();
    CustomerMembershipResponse findById(Integer id);
    CustomerMembershipResponse save(CustomerMembershipRequest customerMembershipRequest);
    CustomerMembershipResponse update(Integer id, CustomerMembershipRequest customerMembershipRequest);
    void delete(Integer id);
    List<CustomerMembershipResponse> findByCustomerId(Integer idCustomer);
    List<CustomerMembershipResponse> findByMembershipStatus(Boolean status);
    List<CustomerMembershipResponse> findByCustomerIdAndStatus(Integer idCustomer, Boolean status);
    List<CustomerMembershipResponse> findActiveMembershipsExpiringSoon();
}