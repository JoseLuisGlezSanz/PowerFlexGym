package com.example.proyecto.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        return ticketRepository.findAll(Sort.by("idTicket").ascending()).stream()
                .map(TicketMapper::toResponse)
                .toList();
    }

    @Override
    public TicketResponse findById(Integer id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + id));
        return TicketMapper.toResponse(ticket);
    }

    @Override
    public TicketResponse save(TicketRequest req) {
        Customer customer = customerRepository.findById(req.getIdCustomer())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + req.getIdCustomer()));
        User user = userRepository.findById(req.getIdUser())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + req.getIdUser()));
        
        Ticket ticket = TicketMapper.toEntity(req);
        ticket.setCustomer(customer);
        ticket.setUser(user);
        
        Ticket savedTicket = ticketRepository.save(ticket);
        return TicketMapper.toResponse(savedTicket);
    }

    @Override
    public TicketResponse update(Integer id, TicketRequest req) {
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + id));

        // Customer customer = customerRepository.findById(req.getIdCustomer())
        //         .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + req.getIdCustomer()));
        
        User user = userRepository.findById(req.getIdUser())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + req.getIdUser()));

        TicketMapper.copyToEntity(req, existingTicket);

        // existingTicket.setCustomer(customer);
        existingTicket.setUser(user);
        Ticket updatedTicket = ticketRepository.save(existingTicket);
        return TicketMapper.toResponse(updatedTicket);
    }

    // @Override
    // public void delete(Integer id) {
    //     ticketRepository.deleteById(id);
    // }

    @Override
    public List<TicketResponse> findByCustomerId(Integer idCustomer) {
        return ticketRepository.findByCustomerId(idCustomer).stream()
                .map(TicketMapper::toResponse)
                .toList();
    }

    @Override
    public List<TicketResponse> findByUserId(Integer idUser) {
        return ticketRepository.findByUserId(idUser).stream()
                .map(TicketMapper::toResponse)
                .toList();
    }

    public List<TicketResponse> getAll(int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize, Sort.by("idTicket").ascending());
        Page<Ticket> tickets = ticketRepository.findAll(pageReq);
        return tickets.getContent().stream().map(TicketMapper::toResponse).toList();
    }
}