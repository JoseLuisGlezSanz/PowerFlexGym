package com.example.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.TicketDetailRequest;
import com.example.proyecto.dto.TicketDetailResponse;
import com.example.proyecto.mapper.TicketDetailMapper;
import com.example.proyecto.model.Product;
import com.example.proyecto.model.Ticket;
import com.example.proyecto.model.TicketDetail;
import com.example.proyecto.repository.ProductRepository;
import com.example.proyecto.repository.TicketDetailRepository;
import com.example.proyecto.repository.TicketRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketDetailServiceImpl implements TicketDetailService{
    private final TicketDetailRepository repository;
    private final ProductRepository productRepository;
    private final TicketRepository ticketRepository;

    @Override
    public List<TicketDetailResponse> findAll() {
        return repository.findAll().stream()
                .map(TicketDetailMapper::toResponse)
                .toList();
    }

    @Override
    public TicketDetailResponse findById(Integer id) {
        TicketDetail ticketDetail = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de ticket no encontrado con ID: " + id));
        return TicketDetailMapper.toResponse(ticketDetail);
    }

    @Override
    public TicketDetailResponse save(TicketDetailRequest req) {
        Product product = productRepository.findById(req.getIdProduct())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + req.getIdProduct()));
        Ticket ticket = ticketRepository.findById(req.getIdTicket())
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + req.getIdTicket()));
        
        TicketDetail ticketDetail = TicketDetailMapper.toEntity(req);
        ticketDetail.setProduct(product);
        ticketDetail.setTicket(ticket);
        
        TicketDetail savedTicketDetail = repository.save(ticketDetail);
        return TicketDetailMapper.toResponse(savedTicketDetail);
    }

    @Override
    public TicketDetailResponse update(Integer id, TicketDetailRequest req) {
        TicketDetail existingTicketDetail = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de ticket no encontrado con ID: " + id));
        
        if (!existingTicketDetail.getProduct().getIdProduct().equals(req.getIdProduct())) {
            Product product = productRepository.findById(req.getIdProduct())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + req.getIdProduct()));
            existingTicketDetail.setProduct(product);
        }
        
        if (!existingTicketDetail.getTicket().getIdTicket().equals(req.getIdTicket())) {
            Ticket ticket = ticketRepository.findById(req.getIdTicket())
                    .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + req.getIdTicket()));
            existingTicketDetail.setTicket(ticket);
        }
        
        TicketDetailMapper.copyToEntity(req, existingTicketDetail);
        TicketDetail updatedTicketDetail = repository.save(existingTicketDetail);
        return TicketDetailMapper.toResponse(updatedTicketDetail);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Detalle de ticket no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<TicketDetailResponse> findByTicketId(Integer idTicket) {
        // Implementar cuando agregues el método al repository
        return repository.findAll().stream()
                .filter(td -> td.getTicket().getIdTicket().equals(idTicket))
                .map(TicketDetailMapper::toResponse)
                .toList();
    }

    @Override
    public List<TicketDetailResponse> findByProductId(Integer idProduct) {
        // Implementar cuando agregues el método al repository
        return repository.findAll().stream()
                .filter(td -> td.getProduct().getIdProduct().equals(idProduct))
                .map(TicketDetailMapper::toResponse)
                .toList();
    }
}