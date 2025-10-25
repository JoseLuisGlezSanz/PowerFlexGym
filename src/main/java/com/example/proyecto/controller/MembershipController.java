package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.MembershipRequest;
import com.example.proyecto.dto.MembershipResponse;
import com.example.proyecto.model.Membership;
import com.example.proyecto.service.MembershipService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/memberships")
@RequiredArgsConstructor
@Tag(name = "Memberships", description = "Provides methods for managing memberships")
public class MembershipController {
    private final MembershipService membershipService;

    @GetMapping
    @Operation(summary = "Get all memberships")
    @ApiResponse(responseCode = "200", description = "List of all memberships", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Membership.class)))})
    public ResponseEntity<List<MembershipResponse>> findAll() {
        List<MembershipResponse> memberships = membershipService.findAll();
        return ResponseEntity.ok(memberships);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get membership by ID")
    @ApiResponse(responseCode = "200", description = "Membership found", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Membership.class)))})
    public ResponseEntity<MembershipResponse> findById(@PathVariable Integer id) {
        MembershipResponse membership = membershipService.findById(id);
        return ResponseEntity.ok(membership);
    }

    @PostMapping
    @Operation(summary = "Create a new membership")
    @ApiResponse(responseCode = "200", description = "Membership created successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Membership.class)))})
    public ResponseEntity<MembershipResponse> create(@RequestBody MembershipRequest membershipRequest) {
        MembershipResponse createdMembership = membershipService.save(membershipRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMembership);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update membership by ID")
    @ApiResponse(responseCode = "200", description = "Membership updated successfully", 
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Membership.class)))})
    public ResponseEntity<MembershipResponse> update(@PathVariable Integer id, @RequestBody MembershipRequest membershipRequest) {
        MembershipResponse updatedMembership = membershipService.update(id, membershipRequest);
        return ResponseEntity.ok(updatedMembership);
    }

    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete membership by ID")
    // @ApiResponse(responseCode = "200", description = "Membership deleted successfully", 
    //         content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Membership.class)))})
    // public ResponseEntity<Void> delete(@PathVariable Integer id) {
    //     membershipService.delete(id);
    //     return ResponseEntity.noContent().build();
    // }

    @GetMapping("/search/{membership}")
    @Operation(summary = "Get memberships by name")
    @ApiResponse(responseCode = "200", description = "List of memberships matching the name",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Membership.class)))})
    public List<MembershipResponse> findByMembership(@PathVariable String membership) {
        return membershipService.findByMembership(membership);
    }

    @GetMapping("/gym/{idGym}")
    @Operation(summary = "Get memberships by gym ID")
    @ApiResponse(responseCode = "200", description = "List of memberships for the specified gym",
            content = {@Content(mediaType = "application/json",array = @ArraySchema(schema = @Schema(implementation = Membership.class)))})
    public List<MembershipResponse> findByGymId(@PathVariable Integer idGym) {
        return membershipService.findByGymId(idGym);
    }
}