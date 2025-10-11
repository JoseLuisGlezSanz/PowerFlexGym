package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import com.example.proyecto.dto.VisitRequest;
import com.example.proyecto.dto.VisitResponse;
import com.example.proyecto.service.VisitService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;

    @GetMapping
    public ResponseEntity<List<VisitResponse>> getAllVisits() {
        List<VisitResponse> visits = visitService.findAll();
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitResponse> getVisitById(@PathVariable Integer id) {
        return visitService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<VisitResponse>> getVisitsByCustomer(@PathVariable Integer customerId) {
        List<VisitResponse> visits = visitService.findByCustomerId(customerId);
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/gym/{gymId}")
    public ResponseEntity<List<VisitResponse>> getVisitsByGym(@PathVariable Integer gymId) {
        List<VisitResponse> visits = visitService.findByGymId(gymId);
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/pending/{pending}")
    public ResponseEntity<List<VisitResponse>> getVisitsByPendingStatus(@PathVariable Integer pending) {
        List<VisitResponse> visits = visitService.findByPendingStatus(pending);
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<VisitResponse>> getVisitsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<VisitResponse> visits = visitService.findByDateRange(startDate, endDate);
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/gym/{gymId}/pending")
    public ResponseEntity<List<VisitResponse>> getPendingVisitsByGym(@PathVariable Integer gymId) {
        List<VisitResponse> visits = visitService.findPendingVisitsByGym(gymId);
        return ResponseEntity.ok(visits);
    }

    @PostMapping
    public ResponseEntity<VisitResponse> createVisit(@Valid @RequestBody VisitRequest request) {
        VisitResponse created = visitService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitResponse> updateVisit(
            @PathVariable Integer id,
            @Valid @RequestBody VisitRequest request) {
        VisitResponse updated = visitService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisit(@PathVariable Integer id) {
        visitService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}/count")
    public ResponseEntity<Long> countVisitsByCustomerAndDateRange(
            @PathVariable Integer customerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Long count = visitService.countVisitsByCustomerAndDateRange(customerId, startDate, endDate);
        return ResponseEntity.ok(count);
    }
}