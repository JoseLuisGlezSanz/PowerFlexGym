package com.example.proyecto.mapper;

import java.time.LocalDateTime;

import com.example.proyecto.dto.CustomerRequest;
import com.example.proyecto.dto.CustomerResponse;
import com.example.proyecto.model.Customer;
import com.example.proyecto.model.EmergencyContact;
import com.example.proyecto.model.Gym;

public class CustomerMapper {
    public static CustomerResponse toResponse(Customer customer) {
        if (customer == null) return null;

        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .cologne(customer.getCologne())
                .phone(customer.getPhone())
                .birthDate(customer.getBirthDate())
                .medicalCondition(customer.getMedicalCondition())
                .photo(customer.getPhoto())
                .photoCredential(customer.getPhotoCredential())
                .verifiedNumber(customer.getVerifiedNumber())
                .gymId(customer.getGym().getId())
                .idContact(customer.getEmergencyContact().getIdContact())
                .registrationDate(customer.getRegistrationDate())
                .build();
    }

    public static Customer toEntity(CustomerRequest customerRequest, Gym gym, EmergencyContact emergencyContact) {
        if (customerRequest == null) return null;
        
        return Customer.builder()
                .name(customerRequest.getName())
                .cologne(customerRequest.getCologne())
                .phone(customerRequest.getPhone())
                .birthDate(customerRequest.getBirthDate())
                .medicalCondition(customerRequest.getMedicalCondition())
                .photo(customerRequest.getPhoto())                  
                .photoCredential(customerRequest.getPhotoCredential()) 
                .verifiedNumber(customerRequest.getVerifiedNumber())
                .emergencyContact(emergencyContact)
                .gym(gym)
                .registrationDate(LocalDateTime.now())
                .build();
    }

    public static void copyToEntity(CustomerRequest customerRequest, Customer entity, Gym gym, EmergencyContact emergencyContact) {
        if (customerRequest == null || entity == null) return;
        
        entity.setName(customerRequest.getName());
        entity.setCologne(customerRequest.getCologne());
        entity.setPhone(customerRequest.getPhone());
        entity.setBirthDate(customerRequest.getBirthDate());
        entity.setMedicalCondition(customerRequest.getMedicalCondition());
        entity.setPhoto(customerRequest.getPhoto());
        entity.setPhotoCredential(customerRequest.getPhotoCredential());
        entity.setVerifiedNumber(customerRequest.getVerifiedNumber());
        entity.setEmergencyContact(emergencyContact);
        entity.setGym(gym);
    }
}