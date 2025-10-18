package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.TicketRequest;
import com.example.proyecto.dto.TicketResponse;
import com.example.proyecto.model.Ticket;
import com.example.proyecto.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Provides methods for managing tickets")
public class TicketController {
    private final TicketService ticketService;

    @GetMapping
    @Operation(summary = "Get all tickets")
    @ApiResponse(responseCode = "200", description = "List of all tickets", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))})
    public ResponseEntity<List<TicketResponse>> findAll() {
        List<TicketResponse> tickets = ticketService.findAll();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket by ID")
    @ApiResponse(responseCode = "200", description = "Ticket found", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))})
    public ResponseEntity<TicketResponse> findById(@PathVariable Integer id) {
        TicketResponse ticket = ticketService.findById(id);
        return ResponseEntity.ok(ticket);
    }

    @PostMapping
    @Operation(summary = "Create a new ticket")
    @ApiResponse(responseCode = "200", description = "Ticket created successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))})
    public ResponseEntity<TicketResponse> create(@RequestBody TicketRequest ticketRequest) {
        TicketResponse createdTicket = ticketService.save(ticketRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update ticket by ID")
    @ApiResponse(responseCode = "200", description = "Ticket updated successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))})
    public ResponseEntity<TicketResponse> update(@PathVariable Integer id, @RequestBody TicketRequest ticketRequest) {
        TicketResponse updatedTicket = ticketService.update(id, ticketRequest);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ticket by ID")
    @ApiResponse(responseCode = "200", description = "Ticket deleted successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))})
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{idCustomer}")
    @Operation(summary = "Get tickets by customer ID")
    @ApiResponse(responseCode = "200", description = "List of tickets for the specified customer", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))})
    public List<TicketResponse> findByCustomerId(@PathVariable Integer idCustomer) {
        return ticketService.findByCustomerId(idCustomer);
    }

    @GetMapping("/user/{idUser}")
    @Operation(summary = "Get tickets by user ID")
    @ApiResponse(responseCode = "200", description = "List of tickets for the specified user", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))})
    public List<TicketResponse> findByUserId(@PathVariable Integer idUser) {
        return ticketService.findByUserId(idUser);
    }
}