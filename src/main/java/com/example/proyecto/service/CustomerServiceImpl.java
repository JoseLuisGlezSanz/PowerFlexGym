package com.example.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.CustomerRequest;
import com.example.proyecto.dto.CustomerResponse;
import com.example.proyecto.mapper.CustomerMapper;
import com.example.proyecto.model.Customer;
import com.example.proyecto.model.Gym;
import com.example.proyecto.repository.CustomerRepository;
import com.example.proyecto.repository.GymRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository repository;
    private final GymRepository gymRepository;

    @Override
    public List<CustomerResponse> findAll() {
        return repository.findAll().stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    @Override
    public CustomerResponse findById(Integer id) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        return CustomerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse save(CustomerRequest req) {
        Gym gym = gymRepository.findById(req.getIdGym())
            .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + req.getIdGym()));

        Customer customer = CustomerMapper.toEntity(req);
        customer.setGym(gym);
        Customer savedCustomer = repository.save(customer);
        return CustomerMapper.toResponse(savedCustomer);
    }

    @Override
    public CustomerResponse update(Integer id, CustomerRequest req) {
        Customer existingCustomer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        
        CustomerMapper.copyToEntity(req, existingCustomer);
        Customer updatedCustomer = repository.save(existingCustomer);
        return CustomerMapper.toResponse(updatedCustomer);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<CustomerResponse> findByName(String name) {
        return repository.findByName(name).stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    @Override
    public List<CustomerResponse> findByVerifiedNumberTrue() {
        return repository.findByVerifiedNumberTrue().stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }
}