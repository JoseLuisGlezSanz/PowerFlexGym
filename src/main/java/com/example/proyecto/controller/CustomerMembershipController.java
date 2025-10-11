package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.CustomerMembershipRequest;
import com.example.proyecto.dto.CustomerMembershipResponse;
import com.example.proyecto.model.CustomerMembershipId;
import com.example.proyecto.service.CustomerMembershipService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer-memberships")
@RequiredArgsConstructor
public class CustomerMembershipController {
    private final CustomerMembershipService customerMembershipService;

    @GetMapping
    public ResponseEntity<List<CustomerMembershipResponse>> getAllCustomerMemberships() {
        List<CustomerMembershipResponse> memberships = customerMembershipService.findAll();
        return ResponseEntity.ok(memberships);
    }

    @GetMapping("/{customerId}/{membershipId}")
    public ResponseEntity<CustomerMembershipResponse> getCustomerMembershipById(
            @PathVariable Integer customerId,
            @PathVariable Integer membershipId) {
        CustomerMembershipId id = new CustomerMembershipId();
        id.setIdCustomer(customerId);
        id.setIdMembership(membershipId);
        
        return customerMembershipService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerMembershipResponse>> getCustomerMembershipsByCustomer(
            @PathVariable Integer customerId) {
        List<CustomerMembershipResponse> memberships = customerMembershipService.findByCustomerId(customerId);
        return ResponseEntity.ok(memberships);
    }

    @GetMapping("/membership/{membershipId}")
    public ResponseEntity<List<CustomerMembershipResponse>> getCustomerMembershipsByMembership(
            @PathVariable Integer membershipId) {
        List<CustomerMembershipResponse> memberships = customerMembershipService.findByMembershipId(membershipId);
        return ResponseEntity.ok(memberships);
    }

    @GetMapping("/gym/{gymId}")
    public ResponseEntity<List<CustomerMembershipResponse>> getCustomerMembershipsByGym(
            @PathVariable Integer gymId) {
        List<CustomerMembershipResponse> memberships = customerMembershipService.findByGymId(gymId);
        return ResponseEntity.ok(memberships);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CustomerMembershipResponse>> getActiveMemberships() {
        List<CustomerMembershipResponse> memberships = customerMembershipService.findActiveMemberships();
        return ResponseEntity.ok(memberships);
    }

    @PostMapping
    public ResponseEntity<CustomerMembershipResponse> createCustomerMembership(
            @Valid @RequestBody CustomerMembershipRequest request) {
        CustomerMembershipResponse created = customerMembershipService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{customerId}/{membershipId}")
    public ResponseEntity<CustomerMembershipResponse> updateCustomerMembership(
            @PathVariable Integer customerId,
            @PathVariable Integer membershipId,
            @Valid @RequestBody CustomerMembershipRequest request) {
        CustomerMembershipId id = new CustomerMembershipId();
        id.setIdCustomer(customerId);
        id.setIdMembership(membershipId);
        
        CustomerMembershipResponse updated = customerMembershipService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{customerId}/{membershipId}")
    public ResponseEntity<Void> deleteCustomerMembership(
            @PathVariable Integer customerId,
            @PathVariable Integer membershipId) {
        CustomerMembershipId id = new CustomerMembershipId();
        id.setIdCustomer(customerId);
        id.setIdMembership(membershipId);
        
        customerMembershipService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{customerId}/{membershipId}/active")
    public ResponseEntity<Boolean> isMembershipActive(
            @PathVariable Integer customerId,
            @PathVariable Integer membershipId) {
        boolean isActive = customerMembershipService.isMembershipActive(customerId, membershipId);
        return ResponseEntity.ok(isActive);
    }
}