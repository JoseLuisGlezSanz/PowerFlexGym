package com.example.proyecto.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.proyecto.dto.CustomerRequest;
import com.example.proyecto.dto.CustomerResponse;
import com.example.proyecto.mapper.CustomerMapper;
import com.example.proyecto.mapper.EmergencyContactMapper;
import com.example.proyecto.model.Customer;
import com.example.proyecto.model.EmergencyContact;
import com.example.proyecto.model.Gym;
import com.example.proyecto.repository.CustomerRepository;
import com.example.proyecto.repository.GymRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final GymRepository gymRepository;

    @Override
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll(Sort.by("idCustomer").ascending()).stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    @Override
    public CustomerResponse findById(Integer id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        return CustomerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse save(CustomerRequest customerRequest) {
        Gym gym = gymRepository.findById(customerRequest.getIdGym())
            .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + customerRequest.getIdGym()));

        Customer customer = CustomerMapper.toEntity(customerRequest);
        customer.setGym(gym);

        EmergencyContact emergencyContact = EmergencyContactMapper.toEntity(
            customerRequest.getContactName(), 
            customerRequest.getContactPhone(),
            customer
        );

        customer.setEmergencyContact(emergencyContact);

        Customer savedCustomer = customerRepository.save(customer);
        return CustomerMapper.toResponse(savedCustomer);
    }

    @Override
    public CustomerResponse update(Integer id, CustomerRequest req) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        
        Gym gym = gymRepository.findById(req.getIdGym())
            .orElseThrow(() -> new RuntimeException("Gym no encontrado con ID: " + req.getIdGym()));
        
        CustomerMapper.copyToEntity(req, existingCustomer);

        existingCustomer.setGym(gym);
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return CustomerMapper.toResponse(updatedCustomer);
    }

    @Override
    public List<CustomerResponse> findByName(String name) {
        return customerRepository.findByName(name).stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    @Override
    public List<CustomerResponse> findByVerifiedNumberTrue() {
        return customerRepository.findByVerifiedNumberTrue(Sort.by("id_Customer").ascending()).stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    public List<CustomerResponse> getAll(int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize, Sort.by("idCustomer").ascending());
        Page<Customer> customers = customerRepository.findAll(pageReq);
        return customers.getContent().stream().map(CustomerMapper::toResponse).toList();
    }

    public List<CustomerResponse> getByVerifiedNumberTrue(int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize, Sort.by("id_Customer").ascending());
        Page<Customer> customers = customerRepository.findByVerifiedNumberTrue(pageReq);
        return customers.getContent().stream().map(CustomerMapper::toResponse).toList();
    }

    @Override
    public List<CustomerResponse> findByGymId(Integer idGym) {
        return customerRepository.findByGymId(idGym).stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    public List<CustomerResponse> getByGymId(int page, int pageSize, Integer gymId) {
        PageRequest pageReq = PageRequest.of(page, pageSize, Sort.by("id_Customer").ascending());
        Page<Customer> customers = customerRepository.findByGymId(gymId, pageReq);
        return customers.getContent().stream().map(CustomerMapper::toResponse).toList();
    }
}   