package com.example.proyecto.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.CustomerMembershipRequest;
import com.example.proyecto.dto.CustomerMembershipResponse;
import com.example.proyecto.model.Customer;
import com.example.proyecto.model.CustomerMembership;
import com.example.proyecto.model.CustomerMembershipId;
import com.example.proyecto.model.Gym;
import com.example.proyecto.model.Membership;
import com.example.proyecto.repository.CustomerMembershipRepository;
import com.example.proyecto.repository.CustomerRepository;
import com.example.proyecto.repository.GymRepository;
import com.example.proyecto.repository.MembershipRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerMembershipServiceImpl implements CustomerMembershipService{
    private final CustomerMembershipRepository customerMembershipRepository;
    private final CustomerRepository customerRepository;
    private final MembershipRepository membershipRepository;
    private final GymRepository gymRepository;

    @Override
    public List<CustomerMembershipResponse> findAll() {
        return customerMembershipRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerMembershipResponse> findById(CustomerMembershipId id) {
        return customerMembershipRepository.findById(id)
                .map(this::mapToResponse);
    }

    @Override
    public List<CustomerMembershipResponse> findByCustomerId(Integer customerId) {
        return customerMembershipRepository.findAll().stream()
                .filter(cm -> cm.getCustomer().getIdCustomer().equals(customerId))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerMembershipResponse> findByMembershipId(Integer membershipId) {
        return customerMembershipRepository.findAll().stream()
                .filter(cm -> cm.getMembership().getIdMembership().equals(membershipId))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerMembershipResponse> findByGymId(Integer gymId) {
        return customerMembershipRepository.findAll().stream()
                .filter(cm -> cm.getGym().getIdGym().equals(gymId))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerMembershipResponse> findActiveMemberships() {
        return customerMembershipRepository.findAll().stream()
                .filter(cm -> Boolean.TRUE.equals(cm.getMembershipStatus()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerMembershipResponse create(CustomerMembershipRequest request) {
        Customer customer = customerRepository.findById(request.getIdCustomer())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        Membership membership = membershipRepository.findById(request.getIdMembership())
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada"));
        
        Gym gym = gymRepository.findById(request.getIdGym())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado"));

        CustomerMembership customerMembership = CustomerMembership.builder()
                .customer(customer)
                .membership(membership)
                .gym(gym)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .memberSince(LocalDateTime.now())
                .membershipStatus(request.getMembershipStatus())
                .build();

        CustomerMembership saved = customerMembershipRepository.save(customerMembership);
        return mapToResponse(saved);
    }

    @Override
    public CustomerMembershipResponse update(CustomerMembershipId id, CustomerMembershipRequest request) {
        CustomerMembership existing = customerMembershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relación cliente-membresía no encontrada"));

        existing.setStartDate(request.getStartDate());
        existing.setEndDate(request.getEndDate());
        existing.setMembershipStatus(request.getMembershipStatus());

        CustomerMembership updated = customerMembershipRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public void delete(CustomerMembershipId id) {
        if (!customerMembershipRepository.existsById(id)) {
            throw new RuntimeException("Relación cliente-membresía no encontrada");
        }
        customerMembershipRepository.deleteById(id);
    }

    @Override
    public boolean existsById(CustomerMembershipId id) {
        return customerMembershipRepository.existsById(id);
    }

    @Override
    public boolean isMembershipActive(Integer customerId, Integer membershipId) {
        return customerMembershipRepository.findAll().stream()
                .anyMatch(cm -> cm.getCustomer().getIdCustomer().equals(customerId) &&
                               cm.getMembership().getIdMembership().equals(membershipId) &&
                               Boolean.TRUE.equals(cm.getMembershipStatus()));
    }

    private CustomerMembershipResponse mapToResponse(CustomerMembership customerMembership) {
        return CustomerMembershipResponse.builder()
                .idCustomer(customerMembership.getCustomer().getIdCustomer())
                .idMembership(customerMembership.getMembership().getIdMembership())
                .idGym(customerMembership.getGym().getIdGym())
                .startDate(customerMembership.getStartDate())
                .endDate(customerMembership.getEndDate())
                .memberSince(customerMembership.getMemberSince())
                .membershipStatus(customerMembership.getMembershipStatus())
                .build();
    }
}