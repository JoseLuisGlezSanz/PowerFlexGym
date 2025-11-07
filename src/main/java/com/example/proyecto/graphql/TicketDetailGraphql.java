package com.example.proyecto.graphql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.proyecto.dto.TicketDetailRequest;
import com.example.proyecto.dto.TicketDetailResponse;
import com.example.proyecto.service.TicketDetailService;

@Controller
public class TicketDetailGraphql {
    @Autowired
    private TicketDetailService ticketDetailService;

    @QueryMapping
    public List<TicketDetailResponse> findAllTicketDetails() {
        return ticketDetailService.findAll();
    }

    @QueryMapping
    public TicketDetailResponse findByIdTicketDetail(@Argument Long id) {
        return ticketDetailService.findById(id);
    }

    @MutationMapping
    public TicketDetailResponse createTicketDetail(@Argument TicketDetailRequest ticketDetailRequest) {
        return ticketDetailService.create(ticketDetailRequest);
    }

    @MutationMapping
    public TicketDetailResponse updateTicketDetail(@Argument Long id, @Argument TicketDetailRequest ticketDetailRequest) {
        return ticketDetailService.update(id, ticketDetailRequest);
    }

    @QueryMapping
    public List<TicketDetailResponse> getAllTicketDetailsPaginated(@Argument int page, @Argument int pageSize) {
        return ticketDetailService.getAll(page, pageSize);
    }

    @QueryMapping
    public List<TicketDetailResponse> findByTicketIdTicketDetails(@Argument Long ticketId) {
        return ticketDetailService.findByTicketId(ticketId);
    }
}