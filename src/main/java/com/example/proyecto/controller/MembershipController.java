package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import com.example.proyecto.dto.MembershipRequest;
import com.example.proyecto.dto.MembershipResponse;
import com.example.proyecto.service.MembershipService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/memberships")
@RequiredArgsConstructor
public class MembershipController {
    private final MembershipService membershipService;

    @GetMapping
    public ResponseEntity<List<MembershipResponse>> findAll() {
        List<MembershipResponse> memberships = membershipService.findAll();
        return ResponseEntity.ok(memberships);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembershipResponse> findById(@PathVariable Integer id) {
        MembershipResponse membership = membershipService.findById(id);
        return ResponseEntity.ok(membership);
    }

    @PostMapping
    public ResponseEntity<MembershipResponse> create(@RequestBody MembershipRequest membershipRequest) {
        MembershipResponse createdMembership = membershipService.save(membershipRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMembership);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembershipResponse> update(@PathVariable Integer id, @RequestBody MembershipRequest membershipRequest) {
        MembershipResponse updatedMembership = membershipService.update(id, membershipRequest);
        return ResponseEntity.ok(updatedMembership);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        membershipService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<MembershipResponse>> findByMembershipName(@RequestParam String name) {
        List<MembershipResponse> memberships = membershipService.findByMembershipName(name);
        return ResponseEntity.ok(memberships);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MembershipResponse>> findByStatus(@PathVariable Integer status) {
        List<MembershipResponse> memberships = membershipService.findByStatus(status);
        return ResponseEntity.ok(memberships);
    }

    @GetMapping("/gym/{gymId}")
    public ResponseEntity<List<MembershipResponse>> findByGymId(@PathVariable Integer gymId) {
        List<MembershipResponse> memberships = membershipService.findByGymId(gymId);
        return ResponseEntity.ok(memberships);
    }
}