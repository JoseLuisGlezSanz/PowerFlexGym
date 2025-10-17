package com.example.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.CustomerMembershipRequest;
import com.example.proyecto.dto.CustomerMembershipResponse;
import com.example.proyecto.mapper.CustomerMembershipMapper;
import com.example.proyecto.model.Customer;
import com.example.proyecto.model.CustomerMembership;
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
    private final CustomerMembershipRepository repository;
    private final CustomerRepository customerRepository;
    private final MembershipRepository membershipRepository;
    private final GymRepository gymRepository;

    @Override
    public List<CustomerMembershipResponse> findAll() {
        return repository.findAll().stream()
                .map(CustomerMembershipMapper::toResponse)
                .toList();
    }

    @Override
    public CustomerMembershipResponse findById(Integer id) {
        CustomerMembership customerMembership = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membresía de cliente no encontrada con ID: " + id));
        return CustomerMembershipMapper.toResponse(customerMembership);
    }

    @Override
    public CustomerMembershipResponse save(CustomerMembershipRequest req) {
        Customer customer = customerRepository.findById(req.getIdCustomer())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + req.getIdCustomer()));
        Membership membership = membershipRepository.findById(req.getIdMembership())
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada con ID: " + req.getIdMembership()));
        Gym gym = gymRepository.findById(req.getIdGym())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + req.getIdGym()));
        
        CustomerMembership customerMembership = CustomerMembershipMapper.toEntity(req);
        customerMembership.setCustomer(customer);
        customerMembership.setMembership(membership);
        customerMembership.setGym(gym);
        
        CustomerMembership savedCustomerMembership = repository.save(customerMembership);
        return CustomerMembershipMapper.toResponse(savedCustomerMembership);
    }

    @Override
    public CustomerMembershipResponse update(Integer id, CustomerMembershipRequest req) {
        CustomerMembership existingCustomerMembership = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membresía de cliente no encontrada con ID: " + id));
        
        if (!existingCustomerMembership.getCustomer().getIdCustomer().equals(req.getIdCustomer())) {
            Customer customer = customerRepository.findById(req.getIdCustomer())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + req.getIdCustomer()));
            existingCustomerMembership.setCustomer(customer);
        }
        
        if (!existingCustomerMembership.getMembership().getIdMembership().equals(req.getIdMembership())) {
            Membership membership = membershipRepository.findById(req.getIdMembership())
                    .orElseThrow(() -> new RuntimeException("Membresía no encontrada con ID: " + req.getIdMembership()));
            existingCustomerMembership.setMembership(membership);
        }
        
        if (!existingCustomerMembership.getGym().getIdGym().equals(req.getIdGym())) {
            Gym gym = gymRepository.findById(req.getIdGym())
                    .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + req.getIdGym()));
            existingCustomerMembership.setGym(gym);
        }
        
        CustomerMembershipMapper.copyToEntity(req, existingCustomerMembership);
        CustomerMembership updatedCustomerMembership = repository.save(existingCustomerMembership);
        return CustomerMembershipMapper.toResponse(updatedCustomerMembership);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Membresía de cliente no encontrada con ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<CustomerMembershipResponse> findByCustomerId(Integer idCustomer) {
        return repository.findByCustomerIdCustomer(idCustomer).stream()
                .map(CustomerMembershipMapper::toResponse)
                .toList();
    }

    @Override
    public List<CustomerMembershipResponse> findByMembershipStatus(Boolean status) {
        return repository.findByMembershipStatus(status).stream()
                .map(CustomerMembershipMapper::toResponse)
                .toList();
    }

    @Override
    public List<CustomerMembershipResponse> findByCustomerIdAndStatus(Integer idCustomer, Boolean status) {
        return repository.findByCustomerIdCustomerAndMembershipStatus(idCustomer, status).stream()
                .map(CustomerMembershipMapper::toResponse)
                .toList();
    }

    @Override
    public List<CustomerMembershipResponse> findActiveMembershipsExpiringSoon() {
        // Implementar lógica para membresías activas que expiran pronto
        return repository.findByMembershipStatus(true).stream()
                .filter(cm -> cm.getEndDate() != null && cm.getEndDate().isBefore(java.time.LocalDate.now().plusDays(7)))
                .map(CustomerMembershipMapper::toResponse)
                .toList();
    }
}