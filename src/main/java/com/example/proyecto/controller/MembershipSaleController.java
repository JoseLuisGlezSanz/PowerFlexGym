package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import com.example.proyecto.dto.MembershipSaleRequest;
import com.example.proyecto.dto.MembershipSaleResponse;
import com.example.proyecto.service.MembershipSaleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/membership-sales")
@RequiredArgsConstructor
public class MembershipSaleController {
    private final MembershipSaleService membershipSaleService;

    @GetMapping
    public ResponseEntity<List<MembershipSaleResponse>> getAllMembershipSales() {
        List<MembershipSaleResponse> sales = membershipSaleService.findAll();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembershipSaleResponse> getMembershipSaleById(@PathVariable Integer id) {
        return membershipSaleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<MembershipSaleResponse>> getSalesByCustomer(@PathVariable Integer customerId) {
        List<MembershipSaleResponse> sales = membershipSaleService.findByCustomerId(customerId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MembershipSaleResponse>> getSalesByUser(@PathVariable Integer userId) {
        List<MembershipSaleResponse> sales = membershipSaleService.findByUserId(userId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/gym/{gymId}")
    public ResponseEntity<List<MembershipSaleResponse>> getSalesByGym(@PathVariable Integer gymId) {
        List<MembershipSaleResponse> sales = membershipSaleService.findByGymId(gymId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/membership/{membershipId}")
    public ResponseEntity<List<MembershipSaleResponse>> getSalesByMembership(@PathVariable Integer membershipId) {
        List<MembershipSaleResponse> sales = membershipSaleService.findByMembershipId(membershipId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<MembershipSaleResponse>> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<MembershipSaleResponse> sales = membershipSaleService.findByDateRange(startDate, endDate);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/active")
    public ResponseEntity<List<MembershipSaleResponse>> getActiveSales() {
        List<MembershipSaleResponse> sales = membershipSaleService.findActiveSales();
        return ResponseEntity.ok(sales);
    }

    @PostMapping
    public ResponseEntity<MembershipSaleResponse> createMembershipSale(@Valid @RequestBody MembershipSaleRequest request) {
        MembershipSaleResponse created = membershipSaleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembershipSaleResponse> updateMembershipSale(
            @PathVariable Integer id,
            @Valid @RequestBody MembershipSaleRequest request) {
        MembershipSaleResponse updated = membershipSaleService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembershipSale(@PathVariable Integer id) {
        membershipSaleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sales-total")
    public ResponseEntity<Double> getTotalSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Double total = membershipSaleService.getTotalSalesByDateRange(startDate, endDate);
        return ResponseEntity.ok(total);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<MembershipSaleResponse> cancelSale(@PathVariable Integer id) {
        MembershipSaleResponse cancelled = membershipSaleService.cancelSale(id);
        return ResponseEntity.ok(cancelled);
    }
}