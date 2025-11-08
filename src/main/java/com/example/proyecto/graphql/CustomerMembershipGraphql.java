package com.example.proyecto.graphql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.proyecto.dto.CustomerMembershipRequest;
import com.example.proyecto.dto.CustomerMembershipResponse;
import com.example.proyecto.service.CustomerMembershipService;

@Controller
public class CustomerMembershipGraphql {
    @Autowired
    private CustomerMembershipService customerMembershipService;

    @QueryMapping
    public List<CustomerMembershipResponse> findAllCustomersMemberships() {
        return customerMembershipService.findAll();
    }

    @QueryMapping
    public CustomerMembershipResponse findByIdCustomerMembership(@Argument Long customerId, @Argument Long membershipId) {
        return customerMembershipService.findById(customerId, membershipId);
    }

    @MutationMapping
    public CustomerMembershipResponse createCustomerMembership(@Argument CustomerMembershipRequest customerMembershipRequest) {
        return customerMembershipService.create(customerMembershipRequest);
    }

    @MutationMapping
    public CustomerMembershipResponse updateCustomerMembership(@Argument Long customerId, @Argument Long membershipId, @Argument CustomerMembershipRequest customerMembershipRequest) {
        return customerMembershipService.update(customerId, membershipId, customerMembershipRequest);
    }

    @QueryMapping
    public List<CustomerMembershipResponse> findByCustomerIdCustomerMembership(@Argument Long customerId) {
        return customerMembershipService.findByCustomerId(customerId);
    }

    @QueryMapping
    public List<CustomerMembershipResponse> findByMembershipIdCustomerMembership(@Argument Long membershipId) {
        return customerMembershipService.findByMembershipId(membershipId);
    }

    @QueryMapping
    public List<CustomerMembershipResponse> findByGymIdCustomerMembership(@Argument Long gymId) {
        return customerMembershipService.findByGymId(gymId);
    }
}