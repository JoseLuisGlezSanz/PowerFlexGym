package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.TicketDetailRequest;
import com.example.proyecto.dto.TicketDetailResponse;
import com.example.proyecto.service.TicketDetailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ticket-details")
@RequiredArgsConstructor
public class TicketDetailController {

    private final TicketDetailService ticketDetailService;

    @GetMapping
    public ResponseEntity<List<TicketDetailResponse>> findAll() {
        List<TicketDetailResponse> ticketDetails = ticketDetailService.findAll();
        return ResponseEntity.ok(ticketDetails);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDetailResponse> findById(@PathVariable Integer id) {
        TicketDetailResponse ticketDetail = ticketDetailService.findById(id);
        return ResponseEntity.ok(ticketDetail);
    }

    @PostMapping
    public ResponseEntity<TicketDetailResponse> create(@RequestBody TicketDetailRequest ticketDetailRequest) {
        TicketDetailResponse createdTicketDetail = ticketDetailService.save(ticketDetailRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicketDetail);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDetailResponse> update(
            @PathVariable Integer id, 
            @RequestBody TicketDetailRequest ticketDetailRequest) {
        TicketDetailResponse updatedTicketDetail = ticketDetailService.update(id, ticketDetailRequest);
        return ResponseEntity.ok(updatedTicketDetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        ticketDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<TicketDetailResponse>> findByTicketId(@PathVariable Integer ticketId) {
        List<TicketDetailResponse> ticketDetails = ticketDetailService.findByTicketId(ticketId);
        return ResponseEntity.ok(ticketDetails);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<TicketDetailResponse>> findByProductId(@PathVariable Integer productId) {
        List<TicketDetailResponse> ticketDetails = ticketDetailService.findByProductId(productId);
        return ResponseEntity.ok(ticketDetails);
    }
}