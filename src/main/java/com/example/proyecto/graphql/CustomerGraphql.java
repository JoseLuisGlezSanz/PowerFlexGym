package com.example.proyecto.graphql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.proyecto.dto.CustomerRequest;
import com.example.proyecto.dto.CustomerResponse;
import com.example.proyecto.service.CustomerService;

@Controller
public class CustomerGraphql {
    @Autowired
    private CustomerService customerService;

    @QueryMapping
    public List<CustomerResponse> findAllCustomers() {
        return customerService.findAll();
    }

    @QueryMapping
    public CustomerResponse findByIdCustomer(@Argument Long id) {
        return customerService.findById(id);
    }

    @MutationMapping
    public CustomerResponse createCustomer(@Argument CustomerRequest customerRequest) {
        return customerService.create(customerRequest);
    }

    @MutationMapping
    public CustomerResponse updateCustomer(@Argument Long id, @Argument CustomerRequest customerRequest) {
        return customerService.update(id, customerRequest);
    }

    // @MutationMapping
    // public Boolean deleteCustomer(@Argument Long id) {
    //         customerService.delete(id);
    //         return true;
    // }

    @QueryMapping
    public List<CustomerResponse> findAllCustomersPaginated(@Argument int page, @Argument int pageSize) {
        List<CustomerResponse> customers = customerService.getAll(page, pageSize);
        return customers;
    }

    @QueryMapping
    public List<CustomerResponse> findByNameCustomer(@Argument String name) {
        List<CustomerResponse> customers = customerService.findByName(name);
        return customers;
    }

    @QueryMapping
    public List<CustomerResponse> findCustomerByVerifiedNumberTrue() {
        List<CustomerResponse> customers = customerService.findByVerifiedNumberTrue();
        return customers;
    }

    @QueryMapping
    public List<CustomerResponse> findCustomerByVerifiedNumberTruePaginated(@Argument int page, @Argument int pageSize) {
        List<CustomerResponse> customers = customerService.getByVerifiedNumberTrue(page, pageSize);
        return customers;
    }

    @QueryMapping
    public List<CustomerResponse> findAllCustomersByGymId(@Argument Long gymId) {
        List<CustomerResponse> customers = customerService.findAllCustomersByGymId(gymId);
        return customers;
    }

    @QueryMapping
    public List<CustomerResponse> findAllCustomersByGymIdPaginated(@Argument int page, @Argument int pageSize, @Argument Long gymId) {
        List<CustomerResponse> customers = customerService.getAllCustomersByGymId(page, pageSize, gymId);
        return customers;
    }
}