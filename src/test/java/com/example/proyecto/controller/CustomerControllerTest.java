package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.CustomerRequest;
import com.example.proyecto.dto.CustomerResponse;
import com.example.proyecto.service.CustomerService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

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

    @TestConfiguration
    static class TestConfig {
        @Bean
        CustomerService customerService() {
            return mock(CustomerService.class);
        }
    }

    // Helpers para crear DTOs de prueba
    private CustomerResponse createCustomerResponse(Integer id, String name, String phone) {
        return CustomerResponse.builder()
                .idCustomer(id)
                .name(name)
                .phone(phone)
                .birthDate(LocalDate.of(1990, 1, 1))
                .medicalCondition(false)
                .registrationDate(LocalDateTime.now())
                .verifiedNumber(true)
                .build();
    }

    private CustomerRequest createCustomerRequest(String name, String phone) {
        CustomerRequest request = new CustomerRequest();
        request.setName(name);
        request.setPhone(phone);
        request.setBirthDate(LocalDate.of(1990, 1, 1));
        request.setMedicalCondition(false);
        request.setVerifiedNumber(true);
        request.setIdGym(1);
        return request;
    }

    /*
     * PRUEBA 1: GET /api/v1/customers - Obtener todos los clientes
     * Esta es fundamental porque prueba el endpoint básico de listado
     */
    @Test
    @DisplayName("GET /api/v1/customers → 200 con lista de clientes")
    void findAll_Ok() throws Exception {
        // Arrange
        List<CustomerResponse> mockCustomers = List.of(
            createCustomerResponse(1, "Juan Pérez", "5512345678"),
            createCustomerResponse(2, "María García", "5512345679")
        );
        when(service.findAll()).thenReturn(mockCustomers);

        // Act & Assert
        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Customer identifier:']").value(1))
                .andExpect(jsonPath("$[0].name").value("Juan Pérez"))
                .andExpect(jsonPath("$[1].['Customer identifier:']").value(2))
                .andExpect(jsonPath("$[1].name").value("María García"));
    }

    @Test
    @DisplayName("GET /api/v1/customers → 200 con lista vacía")
    void findAll_Empty() throws Exception {
        // Arrange
        when(service.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * PRUEBA 2: GET /api/v1/customers/{id} - Obtener cliente por ID
     * Fundamental para probar la búsqueda individual y manejo de errores
     */
    @Test
    @DisplayName("GET /api/v1/customers/{id} existente → 200")
    void findById_Ok() throws Exception {
        // Arrange
        CustomerResponse mockCustomer = createCustomerResponse(1, "Juan Pérez", "5512345678");
        when(service.findById(1)).thenReturn(mockCustomer);

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Customer identifier:']").value(1))
                .andExpect(jsonPath("$.name").value("Juan Pérez"))
                .andExpect(jsonPath("$.phone").value("5512345678"));
    }

    @Test
    @DisplayName("GET /api/v1/customers/{id} no existente → 404")
    void findById_NotFound() throws Exception {
        // Arrange
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Customer not found"));

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
    }

    /*
     * PRUEBA 3: POST /api/v1/customers - Crear nuevo cliente
     * Crítica para probar la creación con validación de entrada
     */
    @Test
    @DisplayName("POST /api/v1/customers → 201 creado exitosamente")
    void create_Ok() throws Exception {
        // Arrange
        CustomerRequest request = createCustomerRequest("Ana López", "5512345670");
        CustomerResponse response = createCustomerResponse(123, "Ana López", "5512345670");
        
        when(service.save(any(CustomerRequest.class))).thenReturn(response);

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.['Customer identifier:']").value(123))
                .andExpect(jsonPath("$.name").value("Ana López"))
                .andExpect(jsonPath("$.phone").value("5512345670"));
    }

    @Test
    @DisplayName("POST /api/v1/customers con teléfono inválido → 400")
    void create_InvalidPhone() throws Exception {
        // Arrange - teléfono no cumple el patrón \\d{10}
        CustomerRequest invalidRequest = createCustomerRequest("Ana López", "123"); // Teléfono inválido

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    /*
     * PRUEBA 4: PUT /api/v1/customers/{id} - Actualizar cliente existente
     * Importante para probar actualización y manejo de recursos no encontrados
     */
    @Test
    @DisplayName("PUT /api/v1/customers/{id} → 200 actualizado exitosamente")
    void update_Ok() throws Exception {
        // Arrange
        CustomerRequest request = createCustomerRequest("Juan Pérez Actualizado", "5512349999");
        CustomerResponse response = createCustomerResponse(1, "Juan Pérez Actualizado", "5512349999");
        
        when(service.update(eq(1), any(CustomerRequest.class))).thenReturn(response);

        // Act & Assert
        mvc.perform(put(BASE + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Customer identifier:']").value(1))
                .andExpect(jsonPath("$.name").value("Juan Pérez Actualizado"))
                .andExpect(jsonPath("$.phone").value("5512349999"));
    }

    @Test
    @DisplayName("PUT /api/v1/customers/{id} no existente → 404")
    void update_NotFound() throws Exception {
        // Arrange
        CustomerRequest request = createCustomerRequest("Nombre", "5512345678");
        when(service.update(eq(999), any(CustomerRequest.class)))
                .thenThrow(new EntityNotFoundException("Customer not found"));

        // Act & Assert
        mvc.perform(put(BASE + "/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}