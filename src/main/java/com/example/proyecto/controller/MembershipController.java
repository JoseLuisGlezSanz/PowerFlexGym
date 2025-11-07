package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
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
    @ApiResponse(responseCode = "200", description = "List of all memberships", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Membership.class)))})
    public List<MembershipResponse> findAll() {
        return membershipService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get membership by ID")
    @ApiResponse(responseCode = "200", description = "Membership by ID", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Membership.class)))})
    public MembershipResponse findById(@PathVariable Long id) {
        return membershipService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new membership")
    @ApiResponse(responseCode = "200", description = "Membership create", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Membership.class)))})
    public ResponseEntity<MembershipResponse> create(@RequestBody MembershipRequest membershipRequest) {
        MembershipResponse createdMembership = membershipService.create(membershipRequest);
        return ResponseEntity
                .created(URI.create("/api/v1/memberships/" + createdMembership.getId()))
                .body(createdMembership);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update membership by ID")
    @ApiResponse(responseCode = "200", description = "Membership updated successfully", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Membership.class)))})
    public MembershipResponse update(@PathVariable Long id, @RequestBody MembershipRequest membershipRequest) {
        return membershipService.update(id, membershipRequest);
    }

    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete membership by ID")
    // @ApiResponse(responseCode = "200", description = "Membership deleted successfully", 
    //         content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Membership.class)))})
    // public ResponseEntity<Void> delete(@PathVariable Integer id) {
    //     membershipService.delete(id);
    //     return ResponseEntity.noContent().build();
    // }

    @GetMapping("/{name}")
    @Operation(summary = "Get memberships by name")
    @ApiResponse(responseCode = "200", description = "List of memberships by name", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Membership.class)))})
    public List<MembershipResponse> findMembershipByName(@PathVariable String name) {
        return membershipService.findMembershipByName(name);
    }

    @GetMapping("/{idGym}")
    @Operation(summary = "Get memberships by gym ID")
    @ApiResponse(responseCode = "200", description = "List of memberships by gym ID", content = {
            @Content(mediaType = "application/json",array = @ArraySchema(schema = @Schema(implementation = Membership.class)))})
    public List<MembershipResponse> findMembershipByGymId(@PathVariable Long gymId) {
        return membershipService.findMembershipByGymId(gymId);
    }
}