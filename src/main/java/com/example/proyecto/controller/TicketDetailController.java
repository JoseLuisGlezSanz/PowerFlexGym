package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.example.proyecto.dto.TicketDetailRequest;
import com.example.proyecto.dto.TicketDetailResponse;
import com.example.proyecto.service.TicketDetailService;

@RestController
@RequestMapping("/api/ticket-details")
public class TicketDetailController {

    @Autowired
    private TicketDetailService ticketDetailService;

    @GetMapping
    public List<TicketDetailResponse> getAll() {
        return ticketDetailService.findAll();
    }

    @GetMapping("/{id}")
    public TicketDetailResponse getById(@PathVariable Integer id) {
        return ticketDetailService.findById(id);
    }

    @PostMapping
    public TicketDetailResponse create(@RequestBody TicketDetailRequest request) {
        return ticketDetailService.create(request);
    }

    @PutMapping("/{id}")
    public TicketDetailResponse update(@PathVariable Integer id, @RequestBody TicketDetailRequest request) {
        return ticketDetailService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        ticketDetailService.delete(id);
    }
}
