package com.example.proyecto.graphql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.proyecto.dto.MembershipRequest;
import com.example.proyecto.dto.MembershipResponse;
import com.example.proyecto.service.MembershipService;

@Controller
public class MembershipGraphql {
    @Autowired
    private MembershipService membershipService;

    @QueryMapping
    public List<MembershipResponse> findAllMemberships() {
        return membershipService.findAll();
    }

    @QueryMapping
    public MembershipResponse findByIdMembership(@Argument Long id) {
        return membershipService.findById(id);
    }

    @MutationMapping
    public MembershipResponse createMembership(@Argument MembershipRequest membershipRequest) {
        return membershipService.create(membershipRequest);
    }

    @MutationMapping
    public MembershipResponse updateMembership(@Argument Long id, @Argument MembershipRequest membershipRequest) {
        return membershipService.update(id, membershipRequest);
    }

    @QueryMapping
    public List<MembershipResponse> findMembershipByName(@Argument String name) {
        return membershipService.findMembershipByName(name);
    }

    @QueryMapping
    public List<MembershipResponse> findMembershipByGymId(@Argument Long gymId) {
        return membershipService.findMembershipByGymId(gymId);
    }
}