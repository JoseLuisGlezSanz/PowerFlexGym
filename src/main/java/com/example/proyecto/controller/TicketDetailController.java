package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.TicketDetailRequest;
import com.example.proyecto.dto.TicketDetailResponse;
import com.example.proyecto.service.TicketDetailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ticket-details")
@RequiredArgsConstructor
public class TicketDetailController {
    private final TicketDetailService ticketDetailService;

    @GetMapping
    public ResponseEntity<List<TicketDetailResponse>> getAllTicketDetails() {
        List<TicketDetailResponse> details = ticketDetailService.findAll();
        return ResponseEntity.ok(details);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDetailResponse> getTicketDetailById(@PathVariable Integer id) {
        return ticketDetailService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<TicketDetailResponse>> getTicketDetailsByTicket(@PathVariable Integer ticketId) {
        List<TicketDetailResponse> details = ticketDetailService.findByTicketId(ticketId);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<TicketDetailResponse>> getTicketDetailsByProduct(@PathVariable Integer productId) {
        List<TicketDetailResponse> details = ticketDetailService.findByProductId(productId);
        return ResponseEntity.ok(details);
    }

    @PostMapping
    public ResponseEntity<TicketDetailResponse> createTicketDetail(@Valid @RequestBody TicketDetailRequest request) {
        TicketDetailResponse created = ticketDetailService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<TicketDetailResponse>> createMultipleTicketDetails(
            @Valid @RequestBody List<TicketDetailRequest> requests) {
        List<TicketDetailResponse> created = ticketDetailService.createMultiple(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDetailResponse> updateTicketDetail(
            @PathVariable Integer id,
            @Valid @RequestBody TicketDetailRequest request) {
        TicketDetailResponse updated = ticketDetailService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicketDetail(@PathVariable Integer id) {
        ticketDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
