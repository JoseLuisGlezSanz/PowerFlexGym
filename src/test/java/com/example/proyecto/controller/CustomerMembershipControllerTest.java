package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.CustomerMembershipRequest;
import com.example.proyecto.dto.CustomerMembershipResponse;
import com.example.proyecto.service.CustomerMembershipService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerMembershipController.class)
class CustomerMembershipControllerTest {

    @Autowired
    MockMvc mvc;
    
    @Autowired
    ObjectMapper mapper;

    @Autowired
    CustomerMembershipService service;

    private static final String BASE = "/api/v1/customer-memberships";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        CustomerMembershipService customerMembershipService() {
            return mock(CustomerMembershipService.class);
        }
    }

    // Helpers para crear DTOs de prueba
    private CustomerMembershipResponse createCustomerMembershipResponse(Integer idCustomer, Integer idMembership, String customerName, String membershipName) {
        return CustomerMembershipResponse.builder()
                .idCustomer(idCustomer)
                .customerName(customerName)
                .idMembership(idMembership)
                .membershipName(membershipName)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .memberSince(LocalDateTime.now())
                .membershipStatus(true)
                .build();
    }

    private CustomerMembershipRequest createCustomerMembershipRequest(Integer idCustomer, Integer idMembership) {
        CustomerMembershipRequest request = new CustomerMembershipRequest();
        request.setIdCustomer(idCustomer);
        request.setIdMembership(idMembership);
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusMonths(1));
        request.setMembershipStatus(true);
        request.setMemberSince(LocalDateTime.now());
        return request;
    }

    /*
     * PRUEBA 1: GET /api/v1/customer-memberships - Obtener todas las membresías de clientes
     */
    @Test
    @DisplayName("GET /api/v1/customer-memberships → 200 con lista de membresías")
    void findAll_Ok() throws Exception {
        // Arrange
        List<CustomerMembershipResponse> mockMemberships = List.of(
            createCustomerMembershipResponse(1, 1, "Juan Pérez", "Membresía Premium"),
            createCustomerMembershipResponse(2, 1, "María García", "Membresía Básica")
        );
        when(service.findAll()).thenReturn(mockMemberships);

        // Act & Assert
        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Customer identifier:']").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].idMembership").value(1))
                .andExpect(jsonPath("$[0].membershipName").value("Membresía Premium"))
                .andExpect(jsonPath("$[1].['Customer identifier:']").value(2))
                .andExpect(jsonPath("$[1].customerName").value("María García"));
    }

    @Test
    @DisplayName("GET /api/v1/customer-memberships → 200 con lista vacía")
    void findAll_Empty() throws Exception {
        // Arrange
        when(service.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * PRUEBA 2: GET /api/v1/customer-memberships/{idCustomer}/{idMembership} - Búsqueda por IDs compuestos
     * Fundamental por el uso de múltiples path variables
     */
    @Test
    @DisplayName("GET /api/v1/customer-memberships/{idCustomer}/{idMembership} existente → 200")
    void findById_Ok() throws Exception {
        // Arrange
        CustomerMembershipResponse mockMembership = createCustomerMembershipResponse(1, 1, "Juan Pérez", "Membresía Premium");
        when(service.findById(1, 1)).thenReturn(mockMembership);

        // Act & Assert
        mvc.perform(get(BASE + "/{idCustomer}/{idMembership}", 1, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Customer identifier:']").value(1))
                .andExpect(jsonPath("$.customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$.idMembership").value(1))
                .andExpect(jsonPath("$.membershipName").value("Membresía Premium"))
                .andExpect(jsonPath("$.membershipStatus").value(true));
    }

    @Test
    @DisplayName("GET /api/v1/customer-memberships/{idCustomer}/{idMembership} no existente → 404")
    void findById_NotFound() throws Exception {
        // Arrange
        when(service.findById(999, 999)).thenThrow(new EntityNotFoundException("Customer membership not found"));

        // Act & Assert
        mvc.perform(get(BASE + "/{idCustomer}/{idMembership}", 999, 999))
                .andExpect(status().isNotFound());
    }

    /*
     * PRUEBA 3: POST /api/v1/customer-memberships - Crear nueva membresía de cliente
     */
    @Test
    @DisplayName("POST /api/v1/customer-memberships → 201 creado exitosamente")
    void create_Ok() throws Exception {
        // Arrange
        CustomerMembershipRequest request = createCustomerMembershipRequest(1, 1);
        CustomerMembershipResponse response = createCustomerMembershipResponse(1, 1, "Juan Pérez", "Membresía Premium");
        
        when(service.save(any(CustomerMembershipRequest.class))).thenReturn(response);

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.['Customer identifier:']").value(1))
                .andExpect(jsonPath("$.customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$.idMembership").value(1))
                .andExpect(jsonPath("$.membershipName").value("Membresía Premium"))
                .andExpect(jsonPath("$.membershipStatus").value(true));
    }

    /*
     * PRUEBA 4: GET /api/v1/customer-memberships/customer/{idCustomer} - Búsqueda por ID de cliente
     * Importante por ser un endpoint específico de negocio
     */
    @Test
    @DisplayName("GET /api/v1/customer-memberships/customer/{idCustomer} → 200 con membresías del cliente")
    void findByCustomerId_Ok() throws Exception {
        // Arrange
        List<CustomerMembershipResponse> mockMemberships = List.of(
            createCustomerMembershipResponse(1, 1, "Juan Pérez", "Membresía Premium"),
            createCustomerMembershipResponse(1, 2, "Juan Pérez", "Membresía Plus")
        );
        when(service.findByCustomerId(1)).thenReturn(mockMemberships);

        // Act & Assert
        mvc.perform(get(BASE + "/customer/{idCustomer}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Customer identifier:']").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].idMembership").value(1))
                .andExpect(jsonPath("$[1].idMembership").value(2))
                .andExpect(jsonPath("$[1].membershipName").value("Membresía Plus"));
    }

    @Test
    @DisplayName("GET /api/v1/customer-memberships/customer/{idCustomer} → 200 con lista vacía")
    void findByCustomerId_Empty() throws Exception {
        // Arrange
        when(service.findByCustomerId(999)).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE + "/customer/{idCustomer}", 999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}