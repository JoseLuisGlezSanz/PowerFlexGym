package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.VisitRequest;
import com.example.proyecto.dto.VisitResponse;
import com.example.proyecto.service.VisitService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/visits")
@RequiredArgsConstructor
@Tag(name = "Visits", description = "Provides methods for managing visits")
public class VisitController {

    private final VisitService visitService;

    @GetMapping
    public ResponseEntity<List<VisitResponse>> findAll() {
        List<VisitResponse> visits = visitService.findAll();
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitResponse> findById(@PathVariable Integer id) {
        VisitResponse visit = visitService.findById(id);
        return ResponseEntity.ok(visit);
    }

    @PostMapping
    public ResponseEntity<VisitResponse> create(@RequestBody VisitRequest visitRequest) {
        VisitResponse createdVisit = visitService.save(visitRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVisit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitResponse> update(@PathVariable Integer id, @RequestBody VisitRequest visitRequest) {
        VisitResponse updatedVisit = visitService.update(id, visitRequest);
        return ResponseEntity.ok(updatedVisit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        visitService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<VisitResponse>> findByCustomerId(@PathVariable Integer customerId) {
        List<VisitResponse> visits = visitService.findByCustomerId(customerId);
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/gym/{gymId}")
    public ResponseEntity<List<VisitResponse>> findByGymId(@PathVariable Integer gymId) {
        List<VisitResponse> visits = visitService.findByGymId(gymId);
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/pending/{pending}")
    public ResponseEntity<List<VisitResponse>> findByPending(@PathVariable Integer pending) {
        List<VisitResponse> visits = visitService.findByPending(pending);
        return ResponseEntity.ok(visits);
    }
}