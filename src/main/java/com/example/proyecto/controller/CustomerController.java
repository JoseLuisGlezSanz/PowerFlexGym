package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.CustomerRequest;
import com.example.proyecto.dto.CustomerResponse;
import com.example.proyecto.model.Customer;
import com.example.proyecto.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Provides methods for managing customers")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    @Operation(summary = "Get all customers")
    @ApiResponse(responseCode = "200", description = "List of registered customers",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))})
    public ResponseEntity<List<CustomerResponse>> findAll() {
        List<CustomerResponse> customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    @ApiResponse(responseCode = "200", description = "Customer found",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))})
    public ResponseEntity<CustomerResponse> findById(@PathVariable Integer id) {
        CustomerResponse customer = customerService.findById(id);
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    @Operation(summary = "Create a new customer")
    @ApiResponse(responseCode = "200", description = "Customer created successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))})
    public ResponseEntity<CustomerResponse> create(@RequestBody CustomerRequest customerRequest) {
        CustomerResponse createdCustomer = customerService.save(customerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer by ID")
    @ApiResponse(responseCode = "200", description = "Customer updated successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))})
    public ResponseEntity<CustomerResponse> update(@PathVariable Integer id, @RequestBody CustomerRequest customerRequest) {
        CustomerResponse updatedCustomer = customerService.update(id, customerRequest);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer by ID")
    @ApiResponse(responseCode = "200", description = "Customer deleted successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))})
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/{name}")
    @Operation(summary = "Search customers by name")
    @ApiResponse(responseCode = "200", description = "List of customers matching the name", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))})
    public List<CustomerResponse> findByName(@PathVariable String name) {
        return customerService.findByName(name);
    }

    @GetMapping("/verified")
    @Operation(summary = "Get customers with verified number")
    @ApiResponse(responseCode = "200", description = "List of customers with verified number", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))})
    public List<CustomerResponse> findByVerifiedNumberTrue() {
        return customerService.findByVerifiedNumberTrue();
    }
}