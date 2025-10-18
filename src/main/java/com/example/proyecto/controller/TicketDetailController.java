package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    @ApiResponse(responseCode = "200", description = "List of all ticket details", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketDetail.class)))})
    public ResponseEntity<List<TicketDetailResponse>> findAll() {
        List<TicketDetailResponse> ticketDetails = ticketDetailService.findAll();
        return ResponseEntity.ok(ticketDetails);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket detail by ID")
    @ApiResponse(responseCode = "200", description = "Ticket detail found", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketDetail.class)))})
    public ResponseEntity<TicketDetailResponse> findById(@PathVariable Integer id) {
        TicketDetailResponse ticketDetail = ticketDetailService.findById(id);
        return ResponseEntity.ok(ticketDetail);
    }

    @PostMapping
    @Operation(summary = "Create a new ticket detail")
    @ApiResponse(responseCode = "200", description = "Ticket detail created successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketDetail.class)))})
    public ResponseEntity<TicketDetailResponse> create(@RequestBody TicketDetailRequest ticketDetailRequest) {
        TicketDetailResponse createdTicketDetail = ticketDetailService.save(ticketDetailRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicketDetail);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update ticket detail by ID")
    @ApiResponse(responseCode = "200", description = "Ticket detail updated successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketDetail.class)))})
    public ResponseEntity<TicketDetailResponse> update(@PathVariable Integer id, @RequestBody TicketDetailRequest ticketDetailRequest) {
        TicketDetailResponse updatedTicketDetail = ticketDetailService.update(id, ticketDetailRequest);
        return ResponseEntity.ok(updatedTicketDetail);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ticket detail by ID")
    @ApiResponse(responseCode = "200", description = "Delete ticket detail by ID", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketDetail.class)))})
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        ticketDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ticket/{idTicket}")
    @Operation(summary = "Get ticket details by ticket ID")
    @ApiResponse(responseCode = "200", description = "List of ticket details for the specified ticket", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketDetail.class)))})
    public List<TicketDetailResponse> findByTicketId(@PathVariable Integer idTicket) {
        return ticketDetailService.findByTicketId(idTicket);
    }

    @GetMapping("/product/{idProduct}")
    @Operation(summary = "Get ticket details by product ID")
    @ApiResponse(responseCode = "200", description = "List of ticket details for the specified product", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketDetail.class)))})
    public List<TicketDetailResponse> findByProductId(@PathVariable Integer idProduct) {
        return ticketDetailService.findByProductId(idProduct);
    }
}