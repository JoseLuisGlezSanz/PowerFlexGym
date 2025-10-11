package com.example.proyecto.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.CustomerResponse;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.dto.VisitRequest;
import com.example.proyecto.dto.VisitResponse;
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
    private final GymRepository gymRepository;
    private final CustomerRepository customerRepository;

    @Override
    public List<VisitResponse> findAll() {
        return visitRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<VisitResponse> findById(Integer id) {
        return visitRepository.findById(id)
                .map(this::mapToResponse);
    }

    @Override
    public List<VisitResponse> findByCustomerId(Integer customerId) {
        return visitRepository.findByCustomerIdCustomer(customerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitResponse> findByGymId(Integer gymId) {
        return visitRepository.findByGymIdGym(gymId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitResponse> findByPendingStatus(Integer pending) {
        return visitRepository.findByPending(pending).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitResponse> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return visitRepository.findByDateBetween(startDate, endDate).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitResponse> findPendingVisitsByGym(Integer gymId) {
        return visitRepository.findByGymIdGymAndPending(gymId, 1).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public VisitResponse create(VisitRequest request) {
        Customer customer = customerRepository.findById(request.getIdCustomer())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        Gym gym = gymRepository.findById(request.getIdGym())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado"));

        Visit visit = Visit.builder()
                .customer(customer)
                .date(request.getDate() != null ? request.getDate() : LocalDateTime.now())
                .pending(request.getPending() != null ? request.getPending() : 0)
                .gym(gym)
                .build();

        Visit saved = visitRepository.save(visit);
        return mapToResponse(saved);
    }

    @Override
    public VisitResponse update(Integer id, VisitRequest request) {
        Visit existing = visitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visita no encontrada"));

        Customer customer = customerRepository.findById(request.getIdCustomer())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        Gym gym = gymRepository.findById(request.getIdGym())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado"));

        existing.setCustomer(customer);
        existing.setDate(request.getDate());
        existing.setPending(request.getPending());
        existing.setGym(gym);

        Visit updated = visitRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!visitRepository.existsById(id)) {
            throw new RuntimeException("Visita no encontrada");
        }
        visitRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return visitRepository.existsById(id);
    }

    @Override
    public Long countVisitsByCustomerAndDateRange(Integer customerId, LocalDateTime startDate, LocalDateTime endDate) {
        return visitRepository.countVisitsByCustomerAndDateRange(customerId, startDate, endDate);
    }

    private VisitResponse mapToResponse(Visit visit) {
        CustomerResponse customerResponse = CustomerResponse.builder()
                .idCustomer(visit.getCustomer().getIdCustomer())
                .name(visit.getCustomer().getName())
                .cologne(visit.getCustomer().getCologne())
                .phone(visit.getCustomer().getPhone())
                .birthDate(visit.getCustomer().getBirthDate())
                .medicalCondition(visit.getCustomer().getMedicalCondition())
                .registrationDate(visit.getCustomer().getRegistrationDate())
                .photo(visit.getCustomer().getPhoto())
                .photoCredential(visit.getCustomer().getPhotoCredential())
                .verifiedNumber(visit.getCustomer().getVerifiedNumber())
                .gym(GymResponse.builder()
                        .idGym(visit.getCustomer().getGym().getIdGym())
                        .gym(visit.getCustomer().getGym().getGym())
                        .build())
                .build();

        GymResponse gymResponse = GymResponse.builder()
                .idGym(visit.getGym().getIdGym())
                .gym(visit.getGym().getGym())
                .build();

        return VisitResponse.builder()
                .idVisit(visit.getIdVisit())
                .idCustomer(visit.getCustomer().getIdCustomer())
                .date(visit.getDate())
                .pending(visit.getPending())
                .gym(gymResponse)
                .customer(customerResponse)
                .build();
    }
}