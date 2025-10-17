package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.CustomerMembershipRequest;
import com.example.proyecto.dto.CustomerMembershipResponse;
import com.example.proyecto.service.CustomerMembershipService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customer-memberships")
@RequiredArgsConstructor
public class CustomerMembershipController {
    private final CustomerMembershipService customerMembershipService;

    @GetMapping
    public ResponseEntity<List<CustomerMembershipResponse>> findAll() {
        List<CustomerMembershipResponse> customerMemberships = customerMembershipService.findAll();
        return ResponseEntity.ok(customerMemberships);
    }

    @GetMapping("/{idCustomer}/{idMembership}")
    public ResponseEntity<CustomerMembershipResponse> findById(@PathVariable Integer idCustomer, @PathVariable Integer idMembership) {
        CustomerMembershipResponse customerMembership = customerMembershipService.findById(idCustomer, idMembership);
        return ResponseEntity.ok(customerMembership);
    }

    @PostMapping
    public ResponseEntity<CustomerMembershipResponse> create(@RequestBody CustomerMembershipRequest customerMembershipRequest) {
        CustomerMembershipResponse createdCustomerMembership = customerMembershipService.save(customerMembershipRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomerMembership);
    }

    @PutMapping("/{idCustomer}/{idMembership}")
    public ResponseEntity<CustomerMembershipResponse> update(@PathVariable Integer idCustomer, @PathVariable Integer idMembership, @RequestBody CustomerMembershipRequest customerMembershipRequest) {
        CustomerMembershipResponse updatedCustomerMembership = customerMembershipService.update(idCustomer, idMembership, customerMembershipRequest);
        return ResponseEntity.ok(updatedCustomerMembership);
    }

    @DeleteMapping("/{idCustomer}/{idMembership}")
    public ResponseEntity<Void> delete(@PathVariable Integer idCustomer, @PathVariable Integer idMembership) {
        customerMembershipService.delete(idCustomer, idMembership);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerMembershipResponse>> findByCustomerId(@PathVariable Integer customerId) {
        List<CustomerMembershipResponse> customerMemberships = customerMembershipService.findByCustomerId(customerId);
        return ResponseEntity.ok(customerMemberships);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CustomerMembershipResponse>> findByMembershipStatus(@PathVariable Boolean status) {
        List<CustomerMembershipResponse> customerMemberships = customerMembershipService.findByMembershipStatus(status);
        return ResponseEntity.ok(customerMemberships);
    }

    @GetMapping("/customer/{customerId}/status/{status}")
    public ResponseEntity<List<CustomerMembershipResponse>> findByCustomerIdAndStatus(@PathVariable Integer customerId, @PathVariable Boolean status) {
        List<CustomerMembershipResponse> customerMemberships = customerMembershipService.findByCustomerIdAndStatus(customerId, status);
        return ResponseEntity.ok(customerMemberships);
    }

    @GetMapping("/expiring-soon")
    public ResponseEntity<List<CustomerMembershipResponse>> findActiveMembershipsExpiringSoon() {
        List<CustomerMembershipResponse> customerMemberships = customerMembershipService.findActiveMembershipsExpiringSoon();
        return ResponseEntity.ok(customerMemberships);
    }
}