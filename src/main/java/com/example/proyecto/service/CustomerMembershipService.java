package com.example.proyecto.service;

import java.util.List;
import com.example.proyecto.dto.CustomerMembershipRequest;
import com.example.proyecto.dto.CustomerMembershipResponse;


public interface CustomerMembershipService {
    List<CustomerMembershipResponse> findAll();
    CustomerMembershipResponse findById(Integer idCustomer, Integer idMembership);
    CustomerMembershipResponse create(CustomerMembershipRequest request);
    CustomerMembershipResponse update(Integer idCustomer, Integer idMembership, CustomerMembershipRequest request);
    void delete(Integer idCustomer, Integer idMembership);
}