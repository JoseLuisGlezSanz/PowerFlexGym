package com.example.proyecto.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.EmergencyContactRequest;
import com.example.proyecto.dto.EmergencyContactResponse;
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
    private final EmergencyContactRepository emergencyContactRepository;
    private final CustomerRepository customerRepository;

    @Override
    public List<EmergencyContactResponse> findAll() {
        return emergencyContactRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EmergencyContactResponse> findById(Integer id) {
        return emergencyContactRepository.findById(id)
                .map(this::mapToResponse);
    }

    @Override
    public Optional<EmergencyContactResponse> findByCustomerId(Integer customerId) {
        return emergencyContactRepository.findAll().stream()
                .filter(contact -> contact.getCustomer().getIdCustomer().equals(customerId))
                .findFirst()
                .map(this::mapToResponse);
    }

    @Override
    @Transactional
    public EmergencyContactResponse create(EmergencyContactRequest request) {
        Customer customer = customerRepository.findById(request.getIdCustomer())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        if (existsByCustomerId(request.getIdCustomer())) {
            throw new RuntimeException("Ya existe un contacto de emergencia para este cliente");
        }

        EmergencyContact emergencyContact = EmergencyContact.builder()
                .contactName(request.getContactName())
                .contactPhone(request.getContactPhone())
                .customer(customer)
                .build();

        EmergencyContact saved = emergencyContactRepository.save(emergencyContact);
        return mapToResponse(saved);
    }

    @Override
    public EmergencyContactResponse update(Integer id, EmergencyContactRequest request) {
        EmergencyContact existing = emergencyContactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contacto de emergencia no encontrado"));

        Customer customer = customerRepository.findById(request.getIdCustomer())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (!existing.getCustomer().getIdCustomer().equals(request.getIdCustomer()) && 
            existsByCustomerId(request.getIdCustomer())) {
            throw new RuntimeException("Ya existe un contacto de emergencia para este cliente");
        }

        existing.setContactName(request.getContactName());
        existing.setContactPhone(request.getContactPhone());
        existing.setCustomer(customer);

        EmergencyContact updated = emergencyContactRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!emergencyContactRepository.existsById(id)) {
            throw new RuntimeException("Contacto de emergencia no encontrado");
        }
        emergencyContactRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return emergencyContactRepository.existsById(id);
    }

    @Override
    public boolean existsByCustomerId(Integer customerId) {
        return emergencyContactRepository.findAll().stream()
                .anyMatch(contact -> contact.getCustomer().getIdCustomer().equals(customerId));
    }

    private EmergencyContactResponse mapToResponse(EmergencyContact emergencyContact) {
        return EmergencyContactResponse.builder()
                .idContact(emergencyContact.getIdContact())
                .contactName(emergencyContact.getContactName())
                .contactPhone(emergencyContact.getContactPhone())
                .idCustomer(emergencyContact.getCustomer().getIdCustomer())
                .build();
    }
}