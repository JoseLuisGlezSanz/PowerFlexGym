package com.example.proyecto.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        return ticketDetailRepository.findAll(Sort.by("idDetailTicket").ascending()).stream()
                .map(TicketDetailMapper::toResponse)
                .toList();
    }

    @Override
    public TicketDetailResponse findById(Integer id) {
        TicketDetail ticketDetail = ticketDetailRepository.findById(id)
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
        
        TicketDetail savedTicketDetail = ticketDetailRepository.save(ticketDetail);
        return TicketDetailMapper.toResponse(savedTicketDetail);
    }

    @Override
    public TicketDetailResponse update(Integer id, TicketDetailRequest req) {
        TicketDetail existingTicketDetail = ticketDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de ticket no encontrado con ID: " + id));
        
        TicketDetailMapper.copyToEntity(req, existingTicketDetail);
        TicketDetail updatedTicketDetail = ticketDetailRepository.save(existingTicketDetail);
        return TicketDetailMapper.toResponse(updatedTicketDetail);
    }

    // @Override
    // public void delete(Integer id) {
    //     ticketDetailRepository.deleteById(id);
    // }

    @Override
    public List<TicketDetailResponse> findByTicketId(Integer idTicket) {
        return ticketDetailRepository.findByTicketId(idTicket).stream()
                .map(TicketDetailMapper::toResponse)
                .toList();
    }

    // @Override
    // public List<TicketDetailResponse> findByProductId(Integer idProduct) {
    //     return ticketDetailRepository.findByProductId(idProduct).stream()
    //             .map(TicketDetailMapper::toResponse)
    //             .toList();
    // }

    public List<TicketDetailResponse> getAll(int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize, Sort.by("idDetailTicket").ascending());
        Page<TicketDetail> ticketsDetails = ticketDetailRepository.findAll(pageReq);
        return ticketsDetails.getContent().stream().map(TicketDetailMapper::toResponse).toList();
    }
}