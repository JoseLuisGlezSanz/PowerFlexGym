package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.example.proyecto.dto.MembershipSaleRequest;
import com.example.proyecto.dto.MembershipSaleResponse;
import com.example.proyecto.service.MembershipSaleService;

@RestController
@RequestMapping("/api/membership-sales")
public class MembershipSaleController {

    @Autowired
    private MembershipSaleService membershipSaleService;

    @GetMapping
    public List<MembershipSaleResponse> getAll() {
        return membershipSaleService.findAll();
    }

    @GetMapping("/{id}")
    public MembershipSaleResponse getById(@PathVariable Integer id) {
        return membershipSaleService.findById(id);
    }

    @PostMapping
    public MembershipSaleResponse create(@RequestBody MembershipSaleRequest request) {
        return membershipSaleService.create(request);
    }

    @PutMapping("/{id}")
    public MembershipSaleResponse update(@PathVariable Integer id, @RequestBody MembershipSaleRequest request) {
        return membershipSaleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        membershipSaleService.delete(id);
    }
}
