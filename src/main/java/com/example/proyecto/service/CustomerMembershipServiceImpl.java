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
    public CustomerMembershipResponse findById(Long customerId, Long membershipId) {
        CustomerMembershipPk id = new CustomerMembershipPk(customerId, membershipId);
        CustomerMembership customerMembership = customerMembershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membresía de cliente no encontrada con IDs: " + customerId + ", " + membershipId));

        return CustomerMembershipMapper.toResponse(customerMembership);
    }

    @Override
    public CustomerMembershipResponse create(CustomerMembershipRequest customerMembershipRequest) {
        Customer customer = customerRepository.findById(customerMembershipRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + customerMembershipRequest.getCustomerId()));

        Membership membership = membershipRepository.findById(customerMembershipRequest.getMembershipId())
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada con ID: " + customerMembershipRequest.getMembershipId()));

        // Validar que el cliente y la membresía pertenezcan al mismo gym
        validateSameGym(customer, membership);

        Gym gym = gymRepository.findById(customer.getGym().getId())
                .orElseThrow(() -> new RuntimeException("Gym no encontrado con ID: " + customer.getGym().getId()));
        
        CustomerMembership customerMembership = CustomerMembershipMapper.toEntity(customerMembershipRequest, customer, membership, gym);
        
        CustomerMembership savedCustomerMembership = customerMembershipRepository.save(customerMembership);
        return CustomerMembershipMapper.toResponse(savedCustomerMembership);
    }

    @Override
    public CustomerMembershipResponse update(Long customerId, Long membershipId, CustomerMembershipRequest customerMembershipRequest) {
        CustomerMembershipPk id = new CustomerMembershipPk(customerId, membershipId);

        CustomerMembership existingCustomerMembership = customerMembershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membresía de cliente no encontrada"));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + customerId));
        
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada con ID: " + membershipId));
        
        // Validar que el cliente y la membresía pertenezcan al mismo gym
        validateSameGym(customer, membership);
        
        Gym gym = gymRepository.findById(customer.getGym().getId())
                .orElseThrow(() -> new RuntimeException("Gym no encontrado con ID: " + customer.getGym().getId()));

        CustomerMembershipMapper.copyToEntity(customerMembershipRequest, existingCustomerMembership, customer, membership, gym);

        CustomerMembership updatedCustomerMembership = customerMembershipRepository.save(existingCustomerMembership);
        return CustomerMembershipMapper.toResponse(updatedCustomerMembership);
    }

    // @Override
    // public void delete(Integer idCustomer, Integer idMembership) {
    //     CustomerMembershipPk id = new CustomerMembershipPk(idCustomer, idMembership);
    //     customerMembershipRepository.deleteById(id);
    // }

    @Override
    public List<CustomerMembershipResponse> findByCustomerId(Long customerId) {
        List<CustomerMembership> customersMemberships = customerMembershipRepository.findByCustomerId(customerId);
        return customersMemberships.stream().map(CustomerMembershipMapper::toResponse).toList();
    }

    @Override
    public List<CustomerMembershipResponse> findByMembershipId(Long membershipId) {
        List<CustomerMembership> customersMemberships = customerMembershipRepository.findByMembershipId(membershipId);
        return customersMemberships.stream().map(CustomerMembershipMapper::toResponse).toList();
    }

    public List<CustomerMembershipResponse> findByGymId(Long gymId) {
        List<CustomerMembership> customersMemberships = customerMembershipRepository.findByGymId(gymId);
        return customersMemberships.stream().map(CustomerMembershipMapper::toResponse).toList();
    }

    private void validateSameGym(Customer customer, Membership membership) {
        Gym customerGym = customer.getGym();
        Gym membershipGym = membership.getGym();
        
        if (customerGym == null || membershipGym == null) {
            throw new RuntimeException("Tanto el cliente como la membresía deben tener un gym asignado");
        }
        
        if (!customerGym.getId().equals(membershipGym.getId())) {
            throw new RuntimeException(
                "El cliente y la membresía no pertenecen al mismo gym. " +
                "Cliente gym ID: " + customerGym.getId() + ", " +
                "Membresía gym ID: " + membershipGym.getId()
            );
        }
    }
}