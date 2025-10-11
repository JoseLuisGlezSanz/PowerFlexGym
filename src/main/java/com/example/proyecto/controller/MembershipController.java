package com.example.proyecto.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.example.proyecto.dto.MembershipRequest;
import com.example.proyecto.dto.MembershipResponse;
import com.example.proyecto.service.MembershipService;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @GetMapping
    public List<MembershipResponse> getAll() {
        return membershipService.findAll();
    }

    @GetMapping("/{id}")
    public MembershipResponse getById(@PathVariable Integer id) {
        return membershipService.findById(id);
    }

    @PostMapping
    public MembershipResponse create(@RequestBody MembershipRequest request) {
        return membershipService.create(request);
    }

    @PutMapping("/{id}")
    public MembershipResponse update(@PathVariable Integer id, @RequestBody MembershipRequest request) {
        return membershipService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        membershipService.delete(id);
    }
}
