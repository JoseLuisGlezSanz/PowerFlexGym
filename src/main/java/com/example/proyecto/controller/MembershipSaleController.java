package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.MembershipSaleRequest;
import com.example.proyecto.dto.MembershipSaleResponse;
import com.example.proyecto.service.MembershipSaleService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/membership-sales")
@RequiredArgsConstructor
@Tag(name = "MembershipSales", description = "Provides methods for managing membership sales")
public class MembershipSaleController {
    private final MembershipSaleService membershipSaleService;

    @GetMapping
    public ResponseEntity<List<MembershipSaleResponse>> findAll() {
        List<MembershipSaleResponse> sales = membershipSaleService.findAll();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembershipSaleResponse> findById(@PathVariable Integer id) {
        MembershipSaleResponse sale = membershipSaleService.findById(id);
        return ResponseEntity.ok(sale);
    }

    @PostMapping
    public ResponseEntity<MembershipSaleResponse> create(@RequestBody MembershipSaleRequest saleRequest) {
        MembershipSaleResponse createdSale = membershipSaleService.save(saleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembershipSaleResponse> update(@PathVariable Integer id, @RequestBody MembershipSaleRequest saleRequest) {
        MembershipSaleResponse updatedSale = membershipSaleService.update(id, saleRequest);
        return ResponseEntity.ok(updatedSale);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        membershipSaleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<MembershipSaleResponse>> findByCustomerId(@PathVariable Integer customerId) {
        List<MembershipSaleResponse> sales = membershipSaleService.findByCustomerId(customerId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/gym/{gymId}")
    public ResponseEntity<List<MembershipSaleResponse>> findByGymId(@PathVariable Integer gymId) {
        List<MembershipSaleResponse> sales = membershipSaleService.findByGymId(gymId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/not-cancelled")
    public ResponseEntity<List<MembershipSaleResponse>> findNotCancelled() {
        List<MembershipSaleResponse> sales = membershipSaleService.findNotCancelled();
        return ResponseEntity.ok(sales);
    }
}