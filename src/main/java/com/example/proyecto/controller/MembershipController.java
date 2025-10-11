package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.MembershipRequest;
import com.example.proyecto.dto.MembershipResponse;
import com.example.proyecto.service.MembershipService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/memberships")
@RequiredArgsConstructor
public class MembershipController {
    private final MembershipService membershipService;

    @GetMapping
    public ResponseEntity<List<MembershipResponse>> getAllMemberships() {
        List<MembershipResponse> memberships = membershipService.findAll();
        return ResponseEntity.ok(memberships);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembershipResponse> getMembershipById(@PathVariable Integer id) {
        return membershipService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/gym/{gymId}")
    public ResponseEntity<List<MembershipResponse>> getMembershipsByGym(@PathVariable Integer gymId) {
        List<MembershipResponse> memberships = membershipService.findByGymId(gymId);
        return ResponseEntity.ok(memberships);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MembershipResponse>> getMembershipsByStatus(@PathVariable Integer status) {
        List<MembershipResponse> memberships = membershipService.findByStatus(status);
        return ResponseEntity.ok(memberships);
    }

    @GetMapping("/active")
    public ResponseEntity<List<MembershipResponse>> getActiveMemberships() {
        List<MembershipResponse> memberships = membershipService.findActiveMemberships();
        return ResponseEntity.ok(memberships);
    }

    @GetMapping("/search")
    public ResponseEntity<MembershipResponse> getMembershipByNameAndGym(
            @RequestParam String name,
            @RequestParam Integer gymId) {
        return membershipService.findByNameAndGym(name, gymId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MembershipResponse> createMembership(@Valid @RequestBody MembershipRequest request) {
        MembershipResponse created = membershipService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembershipResponse> updateMembership(
            @PathVariable Integer id,
            @Valid @RequestBody MembershipRequest request) {
        MembershipResponse updated = membershipService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembership(@PathVariable Integer id) {
        membershipService.delete(id);
        return ResponseEntity.noContent().build();
    }
}