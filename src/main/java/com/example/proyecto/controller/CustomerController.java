package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
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
    @ApiResponse(responseCode = "200", description = "List of all customers", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))})
    public List<CustomerResponse> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/gym/{gymId}")
    @Operation(summary = "Get all customers by gym ID")
    @ApiResponse(responseCode = "200", description = "List of customers by ID gym", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))})
    public List<CustomerResponse> findByGymId(@PathVariable Long gymId) {
        return customerService.findAllCustomersByGymId(gymId);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get customers by name")
    @ApiResponse(responseCode = "200", description = "List of customers by name", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))})
    public List<CustomerResponse> findByName(@PathVariable String name) {
        return customerService.findByName(name);
    }

    @GetMapping("/verifiedNumber/{verifiedNumber}")
    @Operation(summary = "Get all customers with verified number")
    @ApiResponse(responseCode = "200", description = "List of customers with verified number", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))})
    public List<CustomerResponse> findByVerifiedNumberTrue() {
        return customerService.findByVerifiedNumberTrue();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    @ApiResponse(responseCode = "200", description = "Customer by ID", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))})
    public CustomerResponse findById(@PathVariable Long id) { 
        return customerService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new customer")
    @ApiResponse(responseCode = "200", description = "Customer creation", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))})
    public ResponseEntity<CustomerResponse> create(@RequestBody CustomerRequest customerRequest) {
        CustomerResponse createdCustomer = customerService.create(customerRequest);
        return ResponseEntity
                .created(URI.create("/api/v1/customers/" + createdCustomer.getId()))
                .body(createdCustomer);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update customer by ID")
    @ApiResponse(responseCode = "200", description = "Customer update", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))})
    public CustomerResponse update(@PathVariable Long id, @RequestBody CustomerRequest customerRequest) {
        return customerService.update(id, customerRequest);
    }

    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete customer by ID")
    // @ApiResponse(responseCode = "200", description = "Customer delete", content = 
    //        {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))})
    // public void delete(@PathVariable Long id) {
    //     return ResponseEntity.noContent().build();
    // }

    @GetMapping(value = "paginationAll", params = { "page", "pageSize" })
    @Operation(summary = "Get all customers with pagination")
    @ApiResponse(responseCode = "200", description = "List of all customers paginated", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))})
    public List<CustomerResponse> getAllPaginated(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        List<CustomerResponse> customers = customerService.getAll(page, pageSize);
        return customers;
    }

    @GetMapping(value = "paginationByVerifiedNumberTrue", params = { "page", "pageSize" })
    @Operation(summary = "Get all customers with verified number with pagination")
    @ApiResponse(responseCode = "200", description = "List of customers with verified number paginated", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))})
    public List<CustomerResponse> getByVerifiedNumberTruePaginated(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        List<CustomerResponse> customers = customerService.getByVerifiedNumberTrue(page, pageSize);
        return customers;
    }

    @GetMapping(value = "paginationByGymId", params = { "page", "pageSize" })
    @Operation(summary = "Get all customers by gym ID with pagination")
    @ApiResponse(responseCode = "200", description = "List of customers by ID gym paginated", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))})
    public List<CustomerResponse> getByGymIdPaginated(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page, 
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize, @RequestParam Long gymId) {
        List<CustomerResponse> customers = customerService.getAllCustomersByGymId(page, pageSize, gymId);
        return customers;
    }
}