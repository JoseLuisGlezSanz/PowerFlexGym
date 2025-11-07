package com.example.proyecto.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final TicketDetailRepository ticketDetailRepository;
    private final ProductRepository productRepository;
    private final TicketRepository ticketRepository;

    @Override
    public List<TicketDetailResponse> findAll() {
        return ticketDetailRepository.findAll().stream()
                .map(TicketDetailMapper::toResponse)
                .toList();
    }

    @Override
    public TicketDetailResponse findById(Long id) {
        TicketDetail ticketDetail = ticketDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de ticket no encontrado con ID: " + id));

        return TicketDetailMapper.toResponse(ticketDetail);
    }

    @Override
    public TicketDetailResponse create(TicketDetailRequest ticketDetailRequest) {
        Product product = productRepository.findById(ticketDetailRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + ticketDetailRequest.getProductId()));

        Ticket ticket = ticketRepository.findById(ticketDetailRequest.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + ticketDetailRequest.getTicketId()));
        
        TicketDetail ticketDetail = TicketDetailMapper.toEntity(ticketDetailRequest, product, ticket);
        
        TicketDetail savedTicketDetail = ticketDetailRepository.save(ticketDetail);
        return TicketDetailMapper.toResponse(savedTicketDetail);
    }

    @Override
    public TicketDetailResponse update(Long id, TicketDetailRequest ticketDetailRequest) {
        TicketDetail existingTicketDetail = ticketDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de ticket no encontrado con ID: " + id));

        Product product = productRepository.findById(ticketDetailRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + ticketDetailRequest.getProductId()));

        Ticket ticket = ticketRepository.findById(ticketDetailRequest.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + ticketDetailRequest.getTicketId()));
        
        TicketDetailMapper.copyToEntity(ticketDetailRequest, existingTicketDetail, product, ticket);

        TicketDetail updatedTicketDetail = ticketDetailRepository.save(existingTicketDetail);
        return TicketDetailMapper.toResponse(updatedTicketDetail);
    }

    // @Override
    // public void delete(Integer id) {
    //     ticketDetailRepository.deleteById(id);
    // }

    public List<TicketDetailResponse> getAll(int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        Page<TicketDetail> ticketsDetails = ticketDetailRepository.findAll(pageReq);
        return ticketsDetails.getContent().stream().map(TicketDetailMapper::toResponse).toList();
    }

    @Override
    public List<TicketDetailResponse> findByTicketId(Long ticketId) {
        List<TicketDetail> ticketDetailResponses = ticketDetailRepository.findByTicketId(ticketId);
        return ticketDetailResponses.stream().map(TicketDetailMapper::toResponse).toList();
    }
}