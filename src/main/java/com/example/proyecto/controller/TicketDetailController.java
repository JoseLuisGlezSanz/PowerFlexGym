package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

import com.example.proyecto.dto.TicketDetailRequest;
import com.example.proyecto.dto.TicketDetailResponse;
import com.example.proyecto.model.TicketDetail;
import com.example.proyecto.service.TicketDetailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ticket-details")
@RequiredArgsConstructor
@Tag(name = "TicketDetails", description = "Provides methods for managing ticket details")
public class TicketDetailController {
    private final TicketDetailService ticketDetailService;

    @GetMapping
    @Operation(summary = "Get all ticket details")
    @ApiResponse(responseCode = "200", description = "List of all ticket details", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketDetail.class)))})
    public List<TicketDetailResponse> findAll() {
        return ticketDetailService.findAll();
    }

    @GetMapping("/ticket/{ticketId}")
    @Operation(summary = "Get ticket details by ticket ID")
    @ApiResponse(responseCode = "200", description = "List of ticket details by ticket ID", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketDetail.class)))})
    public List<TicketDetailResponse> findByTicketId(@PathVariable Long ticketId) {
        return ticketDetailService.findByTicketId(ticketId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket detail by ID")
    @ApiResponse(responseCode = "200", description = "Ticket detail by ID", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketDetail.class)))})
    public TicketDetailResponse findById(@PathVariable Long id) {
        return ticketDetailService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new ticket detail")
    @ApiResponse(responseCode = "200", description = "Ticket detail create", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketDetail.class)))})
    public ResponseEntity<TicketDetailResponse> create(@RequestBody TicketDetailRequest ticketDetailRequest) {
        TicketDetailResponse createdtTicketDetail = ticketDetailService.create(ticketDetailRequest);
        return ResponseEntity
                .created(URI.create("/api/v1/ticket-details/" + createdtTicketDetail.getId()))
                .body(createdtTicketDetail);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update ticket detail by ID")
    @ApiResponse(responseCode = "200", description = "Ticket detail update", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketDetail.class)))})
    public TicketDetailResponse update(@PathVariable Long id, @RequestBody TicketDetailRequest ticketDetailRequest) {
        return ticketDetailService.update(id, ticketDetailRequest);
    }

    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete ticket detail by ID")
    // @ApiResponse(responseCode = "200", description = "Delete ticket detail by ID", 
    //         content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketDetail.class)))})
    // public ResponseEntity<Void> delete(@PathVariable Integer id) {
    //     ticketDetailService.delete(id);
    //     return ResponseEntity.noContent().build();
    // }

    @GetMapping(value = "paginationAll", params = { "page", "pageSize" })
    @Operation(summary = "Get all ticket details with pagination")
    @ApiResponse(responseCode = "200", description = "List of all ticket details paginated", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketDetail.class)))})
    public List<TicketDetailResponse> getAllPaginated(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        List<TicketDetailResponse> ticketsDetails = ticketDetailService.getAll(page, pageSize);
        return ticketsDetails;
    }
}