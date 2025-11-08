package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
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
@RequestMapping("/api/v1/customers-memberships")
@RequiredArgsConstructor
@Tag(name = "CustomerMemberships", description = "Provides methods for managing customer memberships")
public class CustomerMembershipController {
    private final CustomerMembershipService customerMembershipService;

    @GetMapping
    @Operation(summary = "Get all customer memberships")
    @ApiResponse(responseCode = "200", description = "List of all customer memberships", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerMembership.class)))})
    public List<CustomerMembershipResponse> findAll() {
        return customerMembershipService.findAll();
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get customer memberships by customer ID")
    @ApiResponse(responseCode = "200", description = "List of customer memberships by customer ID", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerMembership.class)))})
    public List<CustomerMembershipResponse> findByCustomerId(@PathVariable Long customerId) {
        return customerMembershipService.findByCustomerId(customerId);
    }

    @GetMapping("/membership/{membershipId}")
    @Operation(summary = "Get customer memberships by membership ID")
    @ApiResponse(responseCode = "200", description = "List of customer memberships by membership ID", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerMembership.class)))})
    public List<CustomerMembershipResponse> findByMembershipId(@PathVariable Long membershipId) {
        return customerMembershipService.findByMembershipId(membershipId);
    }

    @GetMapping("/gym/{gymId}")
    @Operation(summary = "Get customer memberships by gym ID")
    @ApiResponse(responseCode = "200", description = "List of customer memberships by gym ID", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerMembership.class)))})
    public List<CustomerMembershipResponse> findByGymId(@PathVariable Long gymId) {
        return customerMembershipService.findByGymId(gymId);
    }

    @GetMapping("/{customerId}/{membershipId}")
    @Operation(summary = "Get customer membership by customer ID and membership ID")
    @ApiResponse(responseCode = "200", description = "Customer membership by ID", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerMembership.class)))})
    public CustomerMembershipResponse findById(@PathVariable Long customerId, @PathVariable Long membershipId) {
        return customerMembershipService.findById(customerId, membershipId);
    }

    @PostMapping
    @Operation(summary = "Create a new customer membership")
    @ApiResponse(responseCode = "200", description = "Customer membership create", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerMembership.class)))})
    public ResponseEntity<CustomerMembershipResponse> create(@RequestBody CustomerMembershipRequest customerMembershipRequest) {
        CustomerMembershipResponse createdCustomerMembership = customerMembershipService.create(customerMembershipRequest);
                return ResponseEntity
                .created(URI.create("/api/v1/customers-memberships/" + 
                        createdCustomerMembership.getCustomerId() + "/" + 
                        createdCustomerMembership.getMembershipId()))
                .body(createdCustomerMembership);
    }

    @PutMapping("/{idCustomer}/{idMembership}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update customer membership")
    @ApiResponse(responseCode = "200", description = "Customer membership update", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerMembership.class)))})
    public CustomerMembershipResponse update(@PathVariable Long customerId, @PathVariable Long membershipId, @RequestBody CustomerMembershipRequest customerMembershipRequest) {
        return customerMembershipService.update(customerId, membershipId, customerMembershipRequest);
    }

    // @DeleteMapping("/{idCustomer}/{idMembership}")
    // @Operation(summary = "Delete customer membership")
    // @ApiResponse(responseCode = "200", description = "Customer membership deleted successfully", 
    //         content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomerMembership.class)))})
    // public ResponseEntity<Void> delete(@PathVariable Integer idCustomer, @PathVariable Integer idMembership) {
    //     customerMembershipService.delete(idCustomer, idMembership);
    //     return ResponseEntity.noContent().build();
    // }
}