package com.example.proyecto.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.proyecto.dto.EmergencyContactRequest;
import com.example.proyecto.dto.EmergencyContactResponse;
import com.example.proyecto.mapper.EmergencyContactMapper;
import com.example.proyecto.model.EmergencyContact;
import com.example.proyecto.repository.EmergencyContactRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EmergencyContactServiceImpl implements EmergencyContactService{
    private final EmergencyContactRepository emergencyContactRepository;

    @Override
    public List<EmergencyContactResponse> findAll() {
        return emergencyContactRepository.findAll(Sort.by("idContact").ascending()).stream()
                .map(EmergencyContactMapper::toResponse)
                .toList();
    }

    @Override
    public EmergencyContactResponse findById(Integer id) {
        EmergencyContact contact = emergencyContactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contacto de emergencia no encontrado con ID: " + id));
        return EmergencyContactMapper.toResponse(contact);
    }

    // @Override
    // public EmergencyContactResponse save(EmergencyContactRequest req) {
    //     Customer customer = customerRepository.findById(req.getIdCustomer())
    //             .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + req.getIdCustomer()));
        
    //     EmergencyContact contact = EmergencyContactMapper.toEntity(req);
    //     contact.setCustomer(customer);
    //     EmergencyContact savedContact = emergencyContactRepository.save(contact);
    //     return EmergencyContactMapper.toResponse(savedContact);
    // } 

    @Override
    public EmergencyContactResponse update(Integer id, EmergencyContactRequest req) {
        EmergencyContact existingContact = emergencyContactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contacto de emergencia no encontrado con ID: " + id));
        
        EmergencyContactMapper.copyToEntity(req, existingContact);
        EmergencyContact updatedContact = emergencyContactRepository.save(existingContact);
        return EmergencyContactMapper.toResponse(updatedContact);
    }

    // @Override
    // public void delete(Integer id) {
    //     emergencyContactRepository.deleteById(id);
    // }

    @Override
    public EmergencyContactResponse findByIdCustomer(Integer idCustomer) {
        return emergencyContactRepository.findByIdCustomer(idCustomer)
                .map(EmergencyContactMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Contacto de emergencia no encontrado para el cliente con ID: " + idCustomer));
    }

    public List<EmergencyContactResponse> getAll(int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize, Sort.by("idContact").ascending());
        Page<EmergencyContact> emergencysContacts = emergencyContactRepository.findAll(pageReq);
        return emergencysContacts.getContent().stream().map(EmergencyContactMapper::toResponse).toList();
    }
}