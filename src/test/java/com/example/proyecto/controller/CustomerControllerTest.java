package com.example.proyecto.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

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
}