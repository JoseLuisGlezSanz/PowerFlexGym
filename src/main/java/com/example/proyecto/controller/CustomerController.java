package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.example.proyecto.dto.CustomerRequest;
import com.example.proyecto.dto.CustomerResponse;
import com.example.proyecto.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<CustomerResponse> getAll() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public CustomerResponse getById(@PathVariable Integer id) {
        return customerService.findById(id);
    }

    @PostMapping
    public CustomerResponse create(@RequestBody CustomerRequest request) {
        return customerService.create(request);
    }

    @PutMapping("/{id}")
    public CustomerResponse update(@PathVariable Integer id, @RequestBody CustomerRequest request) {
        return customerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        customerService.delete(id);
    }

    @GetMapping("/{id}/emergency-contacts")
    public List<CustomerResponse> getEmergencyContacts(@PathVariable Integer id) {
        return customerService.getEmergencyContacts(id);
    }
}
