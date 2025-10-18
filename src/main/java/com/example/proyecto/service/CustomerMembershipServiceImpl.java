package com.example.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.CustomerMembershipRequest;
import com.example.proyecto.dto.CustomerMembershipResponse;
import com.example.proyecto.mapper.CustomerMembershipMapper;
import com.example.proyecto.model.Customer;
import com.example.proyecto.model.CustomerMembership;
import com.example.proyecto.model.CustomerMembershipPk;
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
                .map(CustomerMembershipMapper::toResponse)
                .toList();
    }

    @Override
    public CustomerMembershipResponse findById(Integer idCustomer, Integer idMembership) {
        CustomerMembershipPk id = new CustomerMembershipPk(idCustomer, idMembership);
        CustomerMembership customerMembership = customerMembershipRepository.findById(id).orElseThrow(() -> new RuntimeException("Membresía de cliente no encontrada con ID: " + id));
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
        
        CustomerMembership savedCustomerMembership = customerMembershipRepository.save(customerMembership);
        return CustomerMembershipMapper.toResponse(savedCustomerMembership);
    }

    @Override
    public CustomerMembershipResponse update(Integer idCustomer, Integer idMembership, CustomerMembershipRequest req) {
        CustomerMembershipPk id = new CustomerMembershipPk(idCustomer, idMembership);
        CustomerMembership existingCustomerMembership = customerMembershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membresía de cliente no encontrada con ID: " + id));
        
        CustomerMembershipMapper.copyToEntity(req, existingCustomerMembership);
        CustomerMembership updatedCustomerMembership = customerMembershipRepository.save(existingCustomerMembership);
        return CustomerMembershipMapper.toResponse(updatedCustomerMembership);
    }

    @Override
    public void delete(Integer idCustomer, Integer idMembership) {
        CustomerMembershipPk id = new CustomerMembershipPk(idCustomer, idMembership);
        customerMembershipRepository.deleteById(id);
    }

    @Override
    public List<CustomerMembershipResponse> findByCustomerId(Integer idCustomer) {
        return customerMembershipRepository.findByCustomerId(idCustomer).stream()
                .map(CustomerMembershipMapper::toResponse)
                .toList();
    }

    @Override
    public List<CustomerMembershipResponse> findByMembershipStatus(Boolean status) {
        return customerMembershipRepository.findByMembershipStatus(status).stream()
                .map(CustomerMembershipMapper::toResponse)
                .toList();
    }

    @Override
    public List<CustomerMembershipResponse> findByCustomerIdCustomerAndMembershipStatus(Integer idCustomer, Boolean status) {
        return customerMembershipRepository.findByCustomerIdCustomerAndMembershipStatus(idCustomer, status).stream()
                .map(CustomerMembershipMapper::toResponse)
                .toList();
    }

    @Override
    public List<CustomerMembershipResponse> findActiveMembershipsExpiringSoon() {
        return customerMembershipRepository.findActiveMembershipsExpiringSoon().stream()
                .map(CustomerMembershipMapper::toResponse)
                .toList();
    }
}