package com.example.proyecto.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.proyecto.dto.CustomerRequest;
import com.example.proyecto.dto.CustomerResponse;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.model.Gym;
import com.example.proyecto.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = CustomerController.class)
public class CustomerControllerTest {
    
    @Autowired
    MockMvc mvc;
    
    @Autowired
    ObjectMapper mapper;

    @Autowired
    CustomerService service;

    private static final String BASE = "/api/v1/customers";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    /*
     * ===========================
     * Config de test: mock beans
     * ===========================
     */
    @TestConfiguration
    static class TestConfig {
        @Bean
        CustomerService customerService() {
            return mock(CustomerService.class);
        }
    }

    /*
     * ===========================
     * Helpers DTO
     * ===========================
     */
    private CustomerResponse customerResponse(Integer idCustomer, String name, String cologne, String phone, LocalDate birthDate, Boolean medicalCondition, LocalDateTime registrationDate, String photo, String photoCredential, Boolean verifiedNumber, GymResponse gym) {
        CustomerResponse r = new CustomerResponse();
        r.setIdCustomer(idCustomer);
        r.setName(name);
        r.setCologne(cologne);
        r.setPhone(phone);
        r.setBirthDate(birthDate);
        r.setMedicalCondition(medicalCondition);
        r.setRegistrationDate(registrationDate);
        r.setPhoto(photo);
        r.setPhotoCredential(photoCredential);
        r.setVerifiedNumber(verifiedNumber);
        r.setGym(gym);
        return r;
    }

    private CustomerRequest customerRequest(String name, String cologne, String phone, LocalDate birthDate, Boolean medicalCondition, String photo, String photoCredential, Boolean verifiedNumber, Integer idGym) {
        CustomerRequest r = new CustomerRequest();
        r.setName(name);
        r.setCologne(cologne);
        r.setPhone(phone);
        r.setBirthDate(birthDate);
        r.setMedicalCondition(medicalCondition);
        r.setPhoto(photo);
        r.setPhotoCredential(photoCredential);
        r.setVerifiedNumber(verifiedNumber);
        r.setIdGym(idGym);
        return r;
    }
}