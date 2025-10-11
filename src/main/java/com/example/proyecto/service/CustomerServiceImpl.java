package com.example.proyecto.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.proyecto.dto.CustomerRequest;
import com.example.proyecto.dto.CustomerResponse;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.model.Customer;
import com.example.proyecto.model.Gym;
import com.example.proyecto.repository.CustomerRepository;
import com.example.proyecto.repository.GymRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final GymRepository gymRepository;

    @Override
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerResponse> findById(Integer id) {
        return customerRepository.findById(id)
                .map(this::mapToResponse);
    }

    @Override
    public List<CustomerResponse> findByGymId(Integer gymId) {
        return customerRepository.findByGymIdGym(gymId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerResponse> findByPhone(String phone) {
        return customerRepository.findByPhone(phone)
                .map(this::mapToResponse);
    }

    @Override
    public List<CustomerResponse> findByNameContaining(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse create(CustomerRequest request) {
        Gym gym = gymRepository.findById(request.getIdGym())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado"));

        Customer customer = Customer.builder()
                .name(request.getName())
                .cologne(request.getCologne())
                .phone(request.getPhone())
                .birthDate(request.getBirthDate())
                .medicalCondition(request.getMedicalCondition())
                .registrationDate(LocalDateTime.now())
                .photo(request.getPhoto() != null ? request.getPhoto() : "default.jpg")
                .photoCredential(request.getPhotoCredential() != null ? request.getPhotoCredential() : "default_credential.jpg")
                .verifiedNumber(request.getVerifiedNumber() != null ? request.getVerifiedNumber() : false)
                .gym(gym)
                .build();

        Customer saved = customerRepository.save(customer);
        return mapToResponse(saved);
    }

    @Override
    public CustomerResponse update(Integer id, CustomerRequest request) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Gym gym = gymRepository.findById(request.getIdGym())
                .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado"));

        existing.setName(request.getName());
        existing.setCologne(request.getCologne());
        existing.setPhone(request.getPhone());
        existing.setBirthDate(request.getBirthDate());
        existing.setMedicalCondition(request.getMedicalCondition());
        existing.setPhoto(request.getPhoto());
        existing.setPhotoCredential(request.getPhotoCredential());
        existing.setVerifiedNumber(request.getVerifiedNumber());
        existing.setGym(gym);

        Customer updated = customerRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado");
        }
        customerRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return customerRepository.existsById(id);
    }

    @Override
    public Long countByGymId(Integer gymId) {
        return customerRepository.countByGymIdGym(gymId);
    }

    private CustomerResponse mapToResponse(Customer customer) {
        GymResponse gymResponse = GymResponse.builder()
                .idGym(customer.getGym().getIdGym())
                .gym(customer.getGym().getGym())
                .build();

        return CustomerResponse.builder()
                .idCustomer(customer.getIdCustomer())
                .name(customer.getName())
                .cologne(customer.getCologne())
                .phone(customer.getPhone())
                .birthDate(customer.getBirthDate())
                .medicalCondition(customer.getMedicalCondition())
                .registrationDate(customer.getRegistrationDate())
                .photo(customer.getPhoto())
                .photoCredential(customer.getPhotoCredential())
                .verifiedNumber(customer.getVerifiedNumber())
                .gym(gymResponse)
                .build();
    }
}