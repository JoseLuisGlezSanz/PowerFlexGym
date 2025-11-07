package com.example.proyecto.graphql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.proyecto.dto.TicketRequest;
import com.example.proyecto.dto.TicketResponse;
import com.example.proyecto.service.TicketService;

@Controller
public class TicketGraphql {
    @Autowired
    private TicketService ticketService;

    @QueryMapping
    public List<TicketResponse> findAllTickets() {
        return ticketService.findAll();
    }

    @QueryMapping
    public TicketResponse findByIdTicket(@Argument Long id) {
        return ticketService.findById(id);
    }

    @MutationMapping
    public TicketResponse createTicket(@Argument TicketRequest ticketRequest) {
        return ticketService.create(ticketRequest);
    }

    @MutationMapping
    public TicketResponse updateTicket(@Argument Long id, @Argument TicketRequest ticketRequest) {
        return ticketService.update(id, ticketRequest);
    }

    // @MutationMapping
    // public Boolean deleteTicket(@Argument Long id) {
    //         ticketService.delete(id);
    //         return true;
    // }

    @QueryMapping
    public List<TicketResponse> findAllTicketsPaginated(@Argument int page, @Argument int pageSize) {
        List<TicketResponse> tickets = ticketService.getAll(page, pageSize);
        return tickets;
    }

    @QueryMapping
    public List<TicketResponse> findAllTicketsByCustomerId(@Argument Long customerId) {
        List<TicketResponse> tickets = ticketService.findAllTicketsByCustomerId(customerId);
        return tickets;
    }

    @QueryMapping
    public List<TicketResponse> findAllTicketsByUserId(@Argument Long userId) {
        List<TicketResponse> tickets = ticketService.findAllTicketsByUserId(userId);
        return tickets;
    }

    @QueryMapping
    public List<TicketResponse> findAllTicketsByCustomerIdPaginated(@Argument int page, @Argument int pageSize, @Argument Long customerId) {
        List<TicketResponse> tickets = ticketService.getAllTicketsByCustomerId(page, pageSize, customerId);
        return tickets;
    }

    @QueryMapping
    public List<TicketResponse> findAllTicketsByUserIdPaginated(@Argument int page, @Argument int pageSize, @Argument Long userId) {
        List<TicketResponse> tickets = ticketService.getAllTicketsByUserId(page, pageSize, userId);
        return tickets;
    }
}