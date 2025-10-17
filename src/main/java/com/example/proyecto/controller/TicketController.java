package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.TicketRequest;
import com.example.proyecto.dto.TicketResponse;
import com.example.proyecto.service.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<TicketResponse>> findAll() {
        List<TicketResponse> tickets = ticketService.findAll();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> findById(@PathVariable Integer id) {
        TicketResponse ticket = ticketService.findById(id);
        return ResponseEntity.ok(ticket);
    }

    @PostMapping
    public ResponseEntity<TicketResponse> create(@RequestBody TicketRequest ticketRequest) {
        TicketResponse createdTicket = ticketService.save(ticketRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponse> update(
            @PathVariable Integer id, 
            @RequestBody TicketRequest ticketRequest) {
        TicketResponse updatedTicket = ticketService.update(id, ticketRequest);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TicketResponse>> findByCustomerId(@PathVariable Integer customerId) {
        List<TicketResponse> tickets = ticketService.findByCustomerId(customerId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketResponse>> findByUserId(@PathVariable Integer userId) {
        List<TicketResponse> tickets = ticketService.findByUserId(userId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TicketResponse>> findByStatus(@PathVariable Integer status) {
        List<TicketResponse> tickets = ticketService.findByStatus(status);
        return ResponseEntity.ok(tickets);
    }
}