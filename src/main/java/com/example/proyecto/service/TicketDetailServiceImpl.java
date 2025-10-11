package com.example.proyecto.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.ProductResponse;
import com.example.proyecto.dto.TicketDetailRequest;
import com.example.proyecto.dto.TicketDetailResponse;
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
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TicketDetailResponse> findById(Integer id) {
        return ticketDetailRepository.findById(id)
                .map(this::mapToResponse);
    }

    @Override
    public List<TicketDetailResponse> findByTicketId(Integer ticketId) {
        return ticketDetailRepository.findAll().stream()
                .filter(detail -> detail.getTicket().getIdTicket().equals(ticketId))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDetailResponse> findByProductId(Integer productId) {
        return ticketDetailRepository.findAll().stream()
                .filter(detail -> detail.getProduct().getIdProduct().equals(productId))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TicketDetailResponse create(TicketDetailRequest request) {
        Product product = productRepository.findById(request.getIdProduct())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        Ticket ticket = ticketRepository.findById(request.getIdTicket())
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        TicketDetail ticketDetail = TicketDetail.builder()
                .amount(request.getAmount())
                .unitPrice(request.getUnitPrice())
                .subtotal(request.getSubtotal())
                .product(product)
                .ticket(ticket)
                .build();

        TicketDetail saved = ticketDetailRepository.save(ticketDetail);
        return mapToResponse(saved);
    }

    @Override
    public TicketDetailResponse update(Integer id, TicketDetailRequest request) {
        TicketDetail existing = ticketDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de ticket no encontrado"));

        Product product = productRepository.findById(request.getIdProduct())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        Ticket ticket = ticketRepository.findById(request.getIdTicket())
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        existing.setAmount(request.getAmount());
        existing.setUnitPrice(request.getUnitPrice());
        existing.setSubtotal(request.getSubtotal());
        existing.setProduct(product);
        existing.setTicket(ticket);

        TicketDetail updated = ticketDetailRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!ticketDetailRepository.existsById(id)) {
            throw new RuntimeException("Detalle de ticket no encontrado");
        }
        ticketDetailRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return ticketDetailRepository.existsById(id);
    }

    @Override
    public List<TicketDetailResponse> createMultiple(List<TicketDetailRequest> requests) {
        List<TicketDetail> details = requests.stream()
                .map(request -> {
                    Product product = productRepository.findById(request.getIdProduct())
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + request.getIdProduct()));
                    
                    Ticket ticket = ticketRepository.findById(request.getIdTicket())
                            .orElseThrow(() -> new RuntimeException("Ticket no encontrado: " + request.getIdTicket()));

                    return TicketDetail.builder()
                            .amount(request.getAmount())
                            .unitPrice(request.getUnitPrice())
                            .subtotal(request.getSubtotal())
                            .product(product)
                            .ticket(ticket)
                            .build();
                })
                .collect(Collectors.toList());

        List<TicketDetail> savedDetails = ticketDetailRepository.saveAll(details);
        return savedDetails.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TicketDetailResponse mapToResponse(TicketDetail ticketDetail) {
        ProductResponse productResponse = ProductResponse.builder()
                .idProduct(ticketDetail.getProduct().getIdProduct())
                .name(ticketDetail.getProduct().getName())
                .price(ticketDetail.getProduct().getPrice())
                .stock(ticketDetail.getProduct().getStock())
                .status(ticketDetail.getProduct().getStatus())
                .photo(ticketDetail.getProduct().getPhoto())
                .build();

        return TicketDetailResponse.builder()
                .idDatailTicket(ticketDetail.getIdDatailTicket())
                .amount(ticketDetail.getAmount())
                .unitPrice(ticketDetail.getUnitPrice())
                .subtotal(ticketDetail.getSubtotal())
                .product(productResponse)
                .idTicket(ticketDetail.getTicket().getIdTicket())
                .build();
    }
}