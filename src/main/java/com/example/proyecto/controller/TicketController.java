package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
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
    @ApiResponse(responseCode = "200", description = "List of all tickets", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))})
    public List<TicketResponse> findAll() {
        return ticketService.findAll();
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get tickets by customer ID")
    @ApiResponse(responseCode = "200", description = "List of tickets by customer", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))})
    public List<TicketResponse> findAllTicketsByCustomerId(@PathVariable Long customerId) {
        return ticketService.findAllTicketsByCustomerId(customerId);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get tickets by user ID")
    @ApiResponse(responseCode = "200", description = "List of tickets for the specified user", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))})
    public List<TicketResponse> findAllTicketsByUserId(@PathVariable Long userId) {
        return ticketService.findAllTicketsByUserId(userId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket by ID")
    @ApiResponse(responseCode = "200", description = "Ticket by ID", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))})
    public TicketResponse findById(@PathVariable Long id) {
        return ticketService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new ticket")
    @ApiResponse(responseCode = "200", description = "Ticket created successfully", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))})
    public ResponseEntity<TicketResponse> create(@RequestBody TicketRequest ticketRequest) {
        TicketResponse createdTicket = ticketService.create(ticketRequest);
        return ResponseEntity
                .created(URI.create("/api/v1/tickets/" + createdTicket.getId()))
                .body(createdTicket);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update ticket by ID")
    @ApiResponse(responseCode = "200", description = "Ticket update", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))})
    public TicketResponse update(@PathVariable Long id, @RequestBody TicketRequest ticketRequest) {
        return ticketService.update(id, ticketRequest);
    }

    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete ticket by ID")
    // @ApiResponse(responseCode = "200", description = "Ticket delete", 
    //         content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))})
    // public ResponseEntity<Void> delete(@PathVariable Integer id) {
    //     ticketService.delete(id);
    //     return ResponseEntity.noContent().build();
    // }

    @GetMapping(value = "paginationAll", params = { "page", "pageSize" })
    @Operation(summary = "Get all products with pagination")
    public List<TicketResponse> getAllPaginated(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        List<TicketResponse> tickets = ticketService.getAll(page, pageSize);
        return tickets;
    }

    @GetMapping(value = "paginationByCustomerId", params = { "page", "pageSize"})
    @Operation(summary = "Get all products by customer ID with pagination")
    @ApiResponse(responseCode = "200", description = "List of tickets by ID customer paginated", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))})
    public List<TicketResponse> getAllTicketsByCustomerIdPaginated(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize, @RequestParam Long customerId) {
        List<TicketResponse> tickets = ticketService.getAllTicketsByCustomerId(page, pageSize, customerId);
        return tickets;
    }

    @GetMapping(value = "paginationByUserId", params = { "page", "pageSize" })
    @Operation(summary = "Get all products by user ID with pagination")
    @ApiResponse(responseCode = "200", description = "List of tickets by ID user paginated", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))})
    public List<TicketResponse> getAllTicketsByUserIdPaginated(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize, @RequestParam Long userId) {
        List<TicketResponse> tickets = ticketService.getAllTicketsByUserId(page, pageSize, userId);
        return tickets;
    }
}