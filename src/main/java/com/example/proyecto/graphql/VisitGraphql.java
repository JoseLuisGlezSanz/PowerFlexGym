package com.example.proyecto.graphql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.proyecto.dto.VisitRequest;
import com.example.proyecto.dto.VisitResponse;
import com.example.proyecto.service.VisitService;

@Controller
public class VisitGraphql {
    @Autowired
    private VisitService visitService;

    @QueryMapping
    public List<VisitResponse> findAllVisits() {
        return visitService.findAll();
    }

    @QueryMapping
    public VisitResponse findByIdVisit(@Argument Long id) {
        return visitService.findById(id);
    }

    @MutationMapping
    public VisitResponse createVisit(@Argument VisitRequest visitRequest) {
        return visitService.create(visitRequest);
    }

    @MutationMapping
    public VisitResponse updateVisit(@Argument Long id, @Argument VisitRequest visitRequest) {
        return visitService.update(id, visitRequest);
    }

    @QueryMapping
    public List<VisitResponse> findByCustomerIdVisits(@Argument Long customerId) {
        return visitService.findByCustomerId(customerId);
    }

    @QueryMapping
    public List<VisitResponse> findByGymIdVisits(@Argument Long gymId) {
        return visitService.findByGymId(gymId);
    }
}
