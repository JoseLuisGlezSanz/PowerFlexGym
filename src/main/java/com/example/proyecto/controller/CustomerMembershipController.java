package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.CustomerMembershipRequest;
import com.example.proyecto.dto.CustomerMembershipResponse;
import com.example.proyecto.model.CustomerMembership;
import com.example.proyecto.service.CustomerMembershipService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customer-memberships")
@RequiredArgsConstructor
@Tag(name = "CustomerMemberships", description = "Provides methods for managing customer memberships")
public class CustomerMembershipController {
    private final CustomerMembershipService customerMembershipService;

    @GetMapping
    @Operation(summary = "Get all customer memberships")
    @ApiResponse(responseCode = "200", description = "List of all customer memberships", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerMembership.class)))})
    public ResponseEntity<List<CustomerMembershipResponse>> findAll() {
        List<CustomerMembershipResponse> customerMemberships = customerMembershipService.findAll();
        return ResponseEntity.ok(customerMemberships);
    }

    @GetMapping("/{idCustomer}/{idMembership}")
    @Operation(summary = "Get customer membership by customer ID and membership ID")
    @ApiResponse(responseCode = "200", description = "Customer membership found", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerMembership.class)))})
    public ResponseEntity<CustomerMembershipResponse> findById(@PathVariable Integer idCustomer, @PathVariable Integer idMembership) {
        CustomerMembershipResponse customerMembership = customerMembershipService.findById(idCustomer, idMembership);
        return ResponseEntity.ok(customerMembership);
    }

    @PostMapping
    @Operation(summary = "Create a new customer membership")
    @ApiResponse(responseCode = "200", description = "Customer membership created successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerMembership.class)))})
    public ResponseEntity<CustomerMembershipResponse> create(@RequestBody CustomerMembershipRequest customerMembershipRequest) {
        CustomerMembershipResponse createdCustomerMembership = customerMembershipService.save(customerMembershipRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomerMembership);
    }

    @PutMapping("/{idCustomer}/{idMembership}")
    @Operation(summary = "Update customer membership")
    @ApiResponse(responseCode = "200", description = "Customer membership updated successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerMembership.class)))})
    public ResponseEntity<CustomerMembershipResponse> update(@PathVariable Integer idCustomer, @PathVariable Integer idMembership, @RequestBody CustomerMembershipRequest customerMembershipRequest) {
        CustomerMembershipResponse updatedCustomerMembership = customerMembershipService.update(idCustomer, idMembership, customerMembershipRequest);
        return ResponseEntity.ok(updatedCustomerMembership);
    }

    @DeleteMapping("/{idCustomer}/{idMembership}")
    @Operation(summary = "Delete customer membership")
    @ApiResponse(responseCode = "200", description = "Customer membership deleted successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerMembership.class)))})
    public ResponseEntity<Void> delete(@PathVariable Integer idCustomer, @PathVariable Integer idMembership) {
        customerMembershipService.delete(idCustomer, idMembership);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get customer memberships by customer ID")
    @ApiResponse(responseCode = "200", description = "List of customer memberships for the specified customer",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerMembership.class)))})
    public List<CustomerMembershipResponse> findByCustomerId(@PathVariable Integer customerId) {
        return customerMembershipService.findByCustomerId(customerId);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get customer memberships by status")
    @ApiResponse(responseCode = "200", description = "List of customer memberships with the specified status",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerMembership.class)))})
    public List<CustomerMembershipResponse> findByMembershipStatus(@PathVariable Boolean status) {
        return customerMembershipService.findByMembershipStatus(status);
    }

    @GetMapping("/customer/{customerId}/status/{status}")
    @Operation(summary = "Get customer memberships by customer ID and status")
    @ApiResponse(responseCode = "200", description = "List of customer memberships matching customer ID and status",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerMembership.class)))})
    public List<CustomerMembershipResponse> findByCustomerIdAndStatus(@PathVariable Integer customerId, @PathVariable Boolean status) {
        return customerMembershipService.findByCustomerIdCustomerAndMembershipStatus(customerId, status);
    }

    @GetMapping("/expiring-soon")
    @Operation(summary = "Get active memberships expiring soon")
    @ApiResponse(responseCode = "200", description = "List of active memberships that are expiring soon",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerMembership.class)))})
    public List<CustomerMembershipResponse> findActiveMembershipsExpiringSoon() {
        return customerMembershipService.findActiveMembershipsExpiringSoon();
    }
}