package com.example.proyecto.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.CustomerResponse;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.dto.RoleResponse;
import com.example.proyecto.dto.TicketRequest;
import com.example.proyecto.dto.TicketResponse;
import com.example.proyecto.dto.UserResponse;
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
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TicketResponse> findById(Integer id) {
        return ticketRepository.findById(id)
                .map(this::mapToResponse);
    }

    @Override
    public List<TicketResponse> findByUserId(Integer userId) {
        return ticketRepository.findByUserIdUser(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketResponse> findByCustomerId(Integer customerId) {
        return ticketRepository.findByCustomerIdCustomer(customerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketResponse> findByStatus(Integer status) {
        return ticketRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketResponse> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return ticketRepository.findByDateBetween(startDate, endDate).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketResponse> findByPaymentMethod(Integer methodPayment) {
        return ticketRepository.findByMethodPayment(methodPayment).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TicketResponse create(TicketRequest request) {
        Customer customer = customerRepository.findById(request.getIdCustomer())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        User user = userRepository.findById(request.getIdUser())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Ticket ticket = Ticket.builder()
                .date(request.getDate() != null ? request.getDate() : LocalDateTime.now())
                .total(request.getTotal())
                .status(request.getStatus())
                .saleDate(request.getSaleDate())
                .methodPayment(request.getMethodPayment())
                .paymentWith(request.getPaymentWith())
                .customer(customer)
                .user(user)
                .build();

        Ticket saved = ticketRepository.save(ticket);
        return mapToResponse(saved);
    }

    @Override
    public TicketResponse update(Integer id, TicketRequest request) {
        Ticket existing = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        Customer customer = customerRepository.findById(request.getIdCustomer())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        User user = userRepository.findById(request.getIdUser())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        existing.setDate(request.getDate());
        existing.setTotal(request.getTotal());
        existing.setStatus(request.getStatus());
        existing.setSaleDate(request.getSaleDate());
        existing.setMethodPayment(request.getMethodPayment());
        existing.setPaymentWith(request.getPaymentWith());
        existing.setCustomer(customer);
        existing.setUser(user);

        Ticket updated = ticketRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!ticketRepository.existsById(id)) {
            throw new RuntimeException("Ticket no encontrado");
        }
        ticketRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return ticketRepository.existsById(id);
    }

    @Override
    public Double getTotalSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        Double total = ticketRepository.findTotalSalesByDateRange(startDate, endDate);
        return total != null ? total : 0.0;
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        CustomerResponse customerResponse = CustomerResponse.builder()
                .idCustomer(ticket.getCustomer().getIdCustomer())
                .name(ticket.getCustomer().getName())
                .cologne(ticket.getCustomer().getCologne())
                .phone(ticket.getCustomer().getPhone())
                .birthDate(ticket.getCustomer().getBirthDate())
                .medicalCondition(ticket.getCustomer().getMedicalCondition())
                .registrationDate(ticket.getCustomer().getRegistrationDate())
                .photo(ticket.getCustomer().getPhoto())
                .photoCredential(ticket.getCustomer().getPhotoCredential())
                .verifiedNumber(ticket.getCustomer().getVerifiedNumber())
                .gym(GymResponse.builder()
                        .idGym(ticket.getCustomer().getGym().getIdGym())
                        .gym(ticket.getCustomer().getGym().getGym())
                        .build())
                .build();

        UserResponse userResponse = UserResponse.builder()
                .idUser(ticket.getUser().getIdUser())
                .user(ticket.getUser().getUser())
                .mail(ticket.getUser().getMail())
                .phone(ticket.getUser().getPhone())
                .name(ticket.getUser().getName())
                .status(ticket.getUser().getStatus())
                .role(RoleResponse.builder()
                        .idRole(ticket.getUser().getRole().getIdRole())
                        .role(ticket.getUser().getRole().getRole())
                        .status(ticket.getUser().getRole().getStatus())
                        .build())
                .gym(GymResponse.builder()
                        .idGym(ticket.getUser().getGym().getIdGym())
                        .gym(ticket.getUser().getGym().getGym())
                        .build())
                .build();

        return TicketResponse.builder()
                .idTicket(ticket.getIdTicket())
                .date(ticket.getDate())
                .total(ticket.getTotal())
                .status(ticket.getStatus())
                .saleDate(ticket.getSaleDate())
                .methodPayment(ticket.getMethodPayment())
                .paymentWith(ticket.getPaymentWith())
                .customer(customerResponse)
                .user(userResponse)
                .details(List.of())
                .build();
    }
}