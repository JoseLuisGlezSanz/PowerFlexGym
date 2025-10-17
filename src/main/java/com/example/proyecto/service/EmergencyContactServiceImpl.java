package com.example.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.EmergencyContactRequest;
import com.example.proyecto.dto.EmergencyContactResponse;
import com.example.proyecto.mapper.EmergencyContactMapper;
import com.example.proyecto.model.Customer;
import com.example.proyecto.model.EmergencyContact;
import com.example.proyecto.repository.CustomerRepository;
import com.example.proyecto.repository.EmergencyContactRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EmergencyContactServiceImpl implements EmergencyContactService{
    private final EmergencyContactRepository repository;
    private final CustomerRepository customerRepository;

    @Override
    public List<EmergencyContactResponse> findAll() {
        return repository.findAll().stream()
                .map(EmergencyContactMapper::toResponse)
                .toList();
    }

    @Override
    public EmergencyContactResponse findById(Integer id) {
        EmergencyContact contact = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contacto de emergencia no encontrado con ID: " + id));
        return EmergencyContactMapper.toResponse(contact);
    }

    @Override
    public EmergencyContactResponse save(EmergencyContactRequest req) {
        Customer customer = customerRepository.findById(req.getIdCustomer())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + req.getIdCustomer()));
        
        EmergencyContact contact = EmergencyContactMapper.toEntity(req);
        contact.setCustomer(customer);
        
        EmergencyContact savedContact = repository.save(contact);
        return EmergencyContactMapper.toResponse(savedContact);
    }

    @Override
    public EmergencyContactResponse update(Integer id, EmergencyContactRequest req) {
        EmergencyContact existingContact = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contacto de emergencia no encontrado con ID: " + id));
        
        if (!existingContact.getCustomer().getIdCustomer().equals(req.getIdCustomer())) {
            Customer customer = customerRepository.findById(req.getIdCustomer())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + req.getIdCustomer()));
            existingContact.setCustomer(customer);
        }
        
        EmergencyContactMapper.copyToEntity(req, existingContact);
        EmergencyContact updatedContact = repository.save(existingContact);
        return EmergencyContactMapper.toResponse(updatedContact);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Contacto de emergencia no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public EmergencyContactResponse findByIdCustomer(Integer idCustomer) {
        return repository.findByIdCustomer(idCustomer)
                .map(EmergencyContactMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Contacto de emergencia no encontrado para el cliente con ID: " + idCustomer));
    }
}