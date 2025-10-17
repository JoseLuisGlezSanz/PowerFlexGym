package com.example.proyecto.service;

import java.util.List;

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
    private final TicketRepository repository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Override
    public List<TicketResponse> findAll() {
        return repository.findAll().stream()
                .map(TicketMapper::toResponse)
                .toList();
    }

    @Override
    public TicketResponse findById(Integer id) {
        Ticket ticket = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + id));
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
        
        Ticket savedTicket = repository.save(ticket);
        return TicketMapper.toResponse(savedTicket);
    }

    @Override
    public TicketResponse update(Integer id, TicketRequest req) {
        Ticket existingTicket = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + id));
        
        if (!existingTicket.getCustomer().getIdCustomer().equals(req.getIdCustomer())) {
            Customer customer = customerRepository.findById(req.getIdCustomer())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + req.getIdCustomer()));
            existingTicket.setCustomer(customer);
        }
        
        if (!existingTicket.getUser().getIdUser().equals(req.getIdUser())) {
            User user = userRepository.findById(req.getIdUser())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + req.getIdUser()));
            existingTicket.setUser(user);
        }
        
        TicketMapper.copyToEntity(req, existingTicket);
        Ticket updatedTicket = repository.save(existingTicket);
        return TicketMapper.toResponse(updatedTicket);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Ticket no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<TicketResponse> findByCustomerId(Integer idCustomer) {
        return repository.findByCustomerIdCustomer(idCustomer).stream()
                .map(TicketMapper::toResponse)
                .toList();
    }

    @Override
    public List<TicketResponse> findByUserId(Integer idUser) {
        return repository.findByUserIdUser(idUser).stream()
                .map(TicketMapper::toResponse)
                .toList();
    }

    @Override
    public List<TicketResponse> findByStatus(Integer status) {
        // Implementar cuando agregues el método al repository
        return repository.findAll().stream()
                .filter(t -> t.getStatus().equals(status))
                .map(TicketMapper::toResponse)
                .toList();
    }
}