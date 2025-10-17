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
    private final VisitRepository repository;
    private final CustomerRepository customerRepository;
    private final GymRepository gymRepository;

    @Override
    public List<VisitResponse> findAll() {
        return repository.findAll().stream()
                .map(VisitMapper::toResponse)
                .toList();
    }

    @Override
    public VisitResponse findById(Integer id) {
        Visit visit = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visita no encontrada con ID: " + id));
        return VisitMapper.toResponse(visit);
    }

    @Override
    public VisitResponse save(VisitRequest req) {
        Customer customer = customerRepository.findById(req.getIdCustomer())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + req.getIdCustomer()));
        Gym gym = gymRepository.findById(req.getIdGym())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + req.getIdGym()));
        
        Visit visit = VisitMapper.toEntity(req);
        visit.setCustomer(customer);
        visit.setGym(gym);
        
        Visit savedVisit = repository.save(visit);
        return VisitMapper.toResponse(savedVisit);
    }

    @Override
    public VisitResponse update(Integer id, VisitRequest req) {
        Visit existingVisit = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visita no encontrada con ID: " + id));
        
        if (!existingVisit.getCustomer().getIdCustomer().equals(req.getIdCustomer())) {
            Customer customer = customerRepository.findById(req.getIdCustomer())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + req.getIdCustomer()));
            existingVisit.setCustomer(customer);
        }
        
        if (!existingVisit.getGym().getIdGym().equals(req.getIdGym())) {
            Gym gym = gymRepository.findById(req.getIdGym())
                    .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + req.getIdGym()));
            existingVisit.setGym(gym);
        }
        
        VisitMapper.copyToEntity(req, existingVisit);
        Visit updatedVisit = repository.save(existingVisit);
        return VisitMapper.toResponse(updatedVisit);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Visita no encontrada con ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<VisitResponse> findByCustomerId(Integer idCustomer) {
        return repository.findByCustomerIdCustomer(idCustomer).stream()
                .map(VisitMapper::toResponse)
                .toList();
    }

    @Override
    public List<VisitResponse> findByGymId(Integer idGym) {
        return repository.findByGymIdGym(idGym).stream()
                .map(VisitMapper::toResponse)
                .toList();
    }

    @Override
    public List<VisitResponse> findByPending(Integer pending) {
        // Implementar cuando agregues el método al repository
        return repository.findAll().stream()
                .filter(v -> v.getPending().equals(pending))
                .map(VisitMapper::toResponse)
                .toList();
    }
}