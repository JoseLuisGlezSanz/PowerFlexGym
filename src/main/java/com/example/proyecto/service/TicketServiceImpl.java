package com.example.proyecto.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.proyecto.dto.TicketRequest;
import com.example.proyecto.dto.TicketResponse;
import com.example.proyecto.mapper.TicketMapper;
import com.example.proyecto.model.Customer;
import com.example.proyecto.model.Ticket;
import com.example.proyecto.model.User;
import com.example.proyecto.repository.CustomerRepository;
import com.example.proyecto.repository.TicketRepository;
import com.example.proyecto.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService{
    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Override
    public List<TicketResponse> findAll() {
        return ticketRepository.findAll().stream()
                .map(TicketMapper::toResponse)
                .toList();
    }

    @Override
    public TicketResponse findById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + id));
        return TicketMapper.toResponse(ticket);
    }

    @Override
    public TicketResponse create(TicketRequest ticketRequest) {
        Customer customer = customerRepository.findById(ticketRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + ticketRequest.getCustomerId()));

        User user = userRepository.findById(ticketRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + ticketRequest.getUserId()));
        
        Ticket ticket = TicketMapper.toEntity(ticketRequest, customer, user);
        
        Ticket savedTicket = ticketRepository.save(ticket);
        return TicketMapper.toResponse(savedTicket);
    }

    @Override
    public TicketResponse update(Long id, TicketRequest ticketRequest) {
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + id));

        Customer customer = customerRepository.findById(ticketRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + ticketRequest.getCustomerId()));

        User user = userRepository.findById(ticketRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + ticketRequest.getUserId()));

        TicketMapper.copyToEntity(ticketRequest, existingTicket, customer, user);

        Ticket updatedTicket = ticketRepository.save(existingTicket);
        return TicketMapper.toResponse(updatedTicket);
    }

    // @Override
    // public void delete(Integer id) {
    //     ticketRepository.deleteById(id);
    // }

    @Override
    public List<TicketResponse> getAll(int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        Page<Ticket> tickets = ticketRepository.findAll(pageReq);
        return tickets.getContent().stream().map(TicketMapper::toResponse).toList();
    }

    @Override
    public List<TicketResponse> findAllTicketsByCustomerId(Long customerId) {
        List<Ticket> tickets = ticketRepository.findAllTicketsByCustomerId(customerId);
        return tickets.stream().map(TicketMapper::toResponse).toList();
    }

    @Override
    public List<TicketResponse> getAllTicketsByCustomerId(int page, int pageSize, Long customerId) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        Page<Ticket> tickets = ticketRepository.getAllTicketsByCustomerId(customerId, pageReq);
        return tickets.getContent().stream().map(TicketMapper::toResponse).toList();
    }

    @Override
    public List<TicketResponse> findAllTicketsByUserId(Long userId) {
        List<Ticket> tickets = ticketRepository.findAllTicketsByUserId(userId);
        return tickets.stream().map(TicketMapper::toResponse).toList();
    }

    @Override
    public List<TicketResponse> getAllTicketsByUserId(int page, int pageSize, Long userId) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        Page<Ticket> tickets = ticketRepository.getAllTicketsByUserId(userId, pageReq);
        return tickets.getContent().stream().map(TicketMapper::toResponse).toList();
    }
}