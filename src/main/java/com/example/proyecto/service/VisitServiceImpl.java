package com.example.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.VisitRequest;
import com.example.proyecto.dto.VisitResponse;
import com.example.proyecto.mapper.VisitMapper;
import com.example.proyecto.model.Customer;
import com.example.proyecto.model.Gym;
import com.example.proyecto.model.Visit;
import com.example.proyecto.repository.CustomerRepository;
import com.example.proyecto.repository.GymRepository;
import com.example.proyecto.repository.VisitRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VisitServiceImpl implements VisitService{
    private final VisitRepository visitRepository;
    private final CustomerRepository customerRepository;
    private final GymRepository gymRepository;

    @Override
    public List<VisitResponse> findAll() {
        return visitRepository.findAll().stream()
                .map(VisitMapper::toResponse)
                .toList();
    }

    @Override
    public VisitResponse findById(Long id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visita no encontrada con ID: " + id));

        return VisitMapper.toResponse(visit);
    }

    @Override
    public VisitResponse create(VisitRequest visitRequest) {
        Customer customer = customerRepository.findById(visitRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + visitRequest.getCustomerId()));

        Gym gym = gymRepository.findById(visitRequest.getGymId())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + visitRequest.getGymId()));
        
        Visit visit = VisitMapper.toEntity(visitRequest, customer, gym);
        
        Visit savedVisit = visitRepository.save(visit);
        return VisitMapper.toResponse(savedVisit);
    }

    @Override
    public VisitResponse update(Long id, VisitRequest visitRequest) {
        Visit existingVisit = visitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visita no encontrada con ID: " + id));
        
        Customer customer = customerRepository.findById(visitRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + visitRequest.getCustomerId()));

        Gym gym = gymRepository.findById(visitRequest.getGymId())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + visitRequest.getGymId()));
        
        VisitMapper.copyToEntity(visitRequest, existingVisit, customer, gym);

        Visit updatedVisit = visitRepository.save(existingVisit);
        return VisitMapper.toResponse(updatedVisit);
    }

    // @Override
    // public void delete(Long id) {
    //     visitRepository.deleteById(id);
    // }

    @Override
    public List<VisitResponse> findByCustomerId(Long customerId) {
        List<Visit> visits = visitRepository.findByCustomerId(customerId);
        return visits.stream().map(VisitMapper::toResponse).toList();
    }

    @Override
    public List<VisitResponse> findByGymId(Long gymId) {
        List<Visit> visits = visitRepository.findByGymId(gymId);
        return visits.stream().map(VisitMapper::toResponse).toList();
    }
}