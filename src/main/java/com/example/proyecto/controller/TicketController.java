package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.example.proyecto.dto.TicketRequest;
import com.example.proyecto.dto.TicketResponse;
import com.example.proyecto.service.TicketService;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public List<TicketResponse> getAll() {
        return ticketService.findAll();
    }

    @GetMapping("/{id}")
    public TicketResponse getById(@PathVariable Integer id) {
        return ticketService.findById(id);
    }

    @PostMapping
    public TicketResponse create(@RequestBody TicketRequest request) {
        return ticketService.create(request);
    }

    @PutMapping("/{id}")
    public TicketResponse update(@PathVariable Integer id, @RequestBody TicketRequest request) {
        return ticketService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        ticketService.delete(id);
    }
}
