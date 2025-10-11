package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.example.proyecto.dto.CustomerMembershipRequest;
import com.example.proyecto.dto.CustomerMembershipResponse;
import com.example.proyecto.service.CustomerMembershipService;

@RestController
@RequestMapping("/api/customer-memberships")
public class CustomerMembershipController {

    @Autowired
    private CustomerMembershipService customerMembershipService;

    @GetMapping
    public List<CustomerMembershipResponse> getAll() {
        return customerMembershipService.findAll();
    }

    @GetMapping("/{idCustomer}/{idMembership}")
    public CustomerMembershipResponse getById(@PathVariable Integer idCustomer, @PathVariable Integer idMembership) {
        return customerMembershipService.findById(idCustomer, idMembership);
    }

    @PostMapping
    public CustomerMembershipResponse create(@RequestBody CustomerMembershipRequest request) {
        return customerMembershipService.create(request);
    }

    @PutMapping("/{idCustomer}/{idMembership}")
    public CustomerMembershipResponse update(@PathVariable Integer idCustomer,
                                             @PathVariable Integer idMembership,
                                             @RequestBody CustomerMembershipRequest request) {
        return customerMembershipService.update(idCustomer, idMembership, request);
    }

    @DeleteMapping("/{idCustomer}/{idMembership}")
    public void delete(@PathVariable Integer idCustomer, @PathVariable Integer idMembership) {
        customerMembershipService.delete(idCustomer, idMembership);
    }
}
