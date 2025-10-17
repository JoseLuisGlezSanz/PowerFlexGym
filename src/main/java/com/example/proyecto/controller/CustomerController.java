package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.CustomerRequest;
import com.example.proyecto.dto.CustomerResponse;
import com.example.proyecto.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll() {
        List<CustomerResponse> customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable Integer id) {
        CustomerResponse customer = customerService.findById(id);
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@RequestBody CustomerRequest customerRequest) {
        CustomerResponse createdCustomer = customerService.save(customerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(
            @PathVariable Integer id, 
            @RequestBody CustomerRequest customerRequest) {
        CustomerResponse updatedCustomer = customerService.update(id, customerRequest);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<CustomerResponse>> findByName(@RequestParam String name) {
        List<CustomerResponse> customers = customerService.findByName(name);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/verified")
    public ResponseEntity<List<CustomerResponse>> findByVerifiedNumberTrue() {
        List<CustomerResponse> customers = customerService.findByVerifiedNumberTrue();
        return ResponseEntity.ok(customers);
    }
}