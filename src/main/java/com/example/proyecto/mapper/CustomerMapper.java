package com.example.proyecto.mapper;

import com.example.proyecto.dto.CustomerRequest;
import com.example.proyecto.dto.CustomerResponse;
import com.example.proyecto.model.Customer;

public class CustomerMapper {
    public static CustomerResponse toResponse(Customer customer) {
        if (customer == null) return null;
        
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
                .build();
    }

    public static Customer toEntity(CustomerRequest dto) {
        if (dto == null) return null;
        
        return Customer.builder()
                .name(dto.getName())
                .cologne(dto.getCologne())
                .phone(dto.getPhone())
                .birthDate(dto.getBirthDate())
                .medicalCondition(dto.getMedicalCondition())
                .registrationDate(dto.getRegistrationDate())
                .photo(dto.getPhoto())
                .photoCredential(dto.getPhotoCredential())
                .verifiedNumber(dto.getVerifiedNumber())
                .build();
    }

    public static void copyToEntity(CustomerRequest dto, Customer entity) {
        if (dto == null || entity == null) return;
        
        entity.setName(dto.getName());
        entity.setCologne(dto.getCologne());
        entity.setPhone(dto.getPhone());
        entity.setBirthDate(dto.getBirthDate());
        entity.setMedicalCondition(dto.getMedicalCondition());
        entity.setRegistrationDate(dto.getRegistrationDate());
        entity.setPhoto(dto.getPhoto());
        entity.setPhotoCredential(dto.getPhotoCredential());
        entity.setVerifiedNumber(dto.getVerifiedNumber());
    }
}
