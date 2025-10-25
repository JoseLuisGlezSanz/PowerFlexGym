package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.EmergencyContactRequest;
import com.example.proyecto.dto.EmergencyContactResponse;
import com.example.proyecto.service.EmergencyContactService;
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

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmergencyContactController.class)
class EmergencyContactControllerTest {

    @Autowired
    MockMvc mvc;
    
    @Autowired
    ObjectMapper mapper;

    @Autowired
    EmergencyContactService service;

    private static final String BASE = "/api/v1/emergency-contacts";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        EmergencyContactService emergencyContactService() {
            return mock(EmergencyContactService.class);
        }
    }

    // Helpers para crear DTOs de prueba
    private EmergencyContactResponse createEmergencyContactResponse(Integer id, String name, String phone) {
        return EmergencyContactResponse.builder()
                .idContact(id)
                .contactName(name)
                .contactPhone(phone)
                .build();
    }

    private EmergencyContactRequest createEmergencyContactRequest(String name, String phone) {
        EmergencyContactRequest request = new EmergencyContactRequest();
        request.setContactName(name);
        request.setContactPhone(phone);
        return request;
    }

    /*
     * PRUEBA 1: GET /api/v1/emergency-contacts - Obtener todos los contactos de emergencia
     */
    @Test
    @DisplayName("GET /api/v1/emergency-contacts → 200 con lista de contactos")
    void findAll_Ok() throws Exception {
        // Arrange
        List<EmergencyContactResponse> mockContacts = List.of(
            createEmergencyContactResponse(1, "María López", "5512345678"),
            createEmergencyContactResponse(2, "Carlos Ruiz", "5512345679")
        );
        when(service.findAll()).thenReturn(mockContacts);

        // Act & Assert
        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Contact identifier:']").value(1))
                .andExpect(jsonPath("$[0].contactName").value("María López"))
                .andExpect(jsonPath("$[0].contactPhone").value("5512345678"))
                .andExpect(jsonPath("$[1].['Contact identifier:']").value(2))
                .andExpect(jsonPath("$[1].contactName").value("Carlos Ruiz"));
    }

    @Test
    @DisplayName("GET /api/v1/emergency-contacts → 200 con lista vacía")
    void findAll_Empty() throws Exception {
        // Arrange
        when(service.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * PRUEBA 2: GET /api/v1/emergency-contacts/{id} - Obtener contacto por ID
     */
    @Test
    @DisplayName("GET /api/v1/emergency-contacts/{id} existente → 200")
    void findById_Ok() throws Exception {
        // Arrange
        EmergencyContactResponse mockContact = createEmergencyContactResponse(1, "María López", "5512345678");
        when(service.findById(1)).thenReturn(mockContact);

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Contact identifier:']").value(1))
                .andExpect(jsonPath("$.contactName").value("María López"))
                .andExpect(jsonPath("$.contactPhone").value("5512345678"));
    }

    @Test
    @DisplayName("GET /api/v1/emergency-contacts/{id} no existente → 404")
    void findById_NotFound() throws Exception {
        // Arrange
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Emergency contact not found"));

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
    }

    /*
     * PRUEBA 3: PUT /api/v1/emergency-contacts/{id} - Actualizar contacto de emergencia
     * Importante porque es una de las pocas operaciones activas en este controller
     */
    @Test
    @DisplayName("PUT /api/v1/emergency-contacts/{id} → 200 actualizado exitosamente")
    void update_Ok() throws Exception {
        // Arrange
        EmergencyContactRequest request = createEmergencyContactRequest("María López Actualizado", "5598765432");
        EmergencyContactResponse response = createEmergencyContactResponse(1, "María López Actualizado", "5598765432");
        
        when(service.update(eq(1), any(EmergencyContactRequest.class))).thenReturn(response);

        // Act & Assert
        mvc.perform(put(BASE + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Contact identifier:']").value(1))
                .andExpect(jsonPath("$.contactName").value("María López Actualizado"))
                .andExpect(jsonPath("$.contactPhone").value("5598765432"));
    }

    @Test
    @DisplayName("PUT /api/v1/emergency-contacts/{id} no existente → 404")
    void update_NotFound() throws Exception {
        // Arrange
        EmergencyContactRequest request = createEmergencyContactRequest("Nombre", "5512345678");
        when(service.update(eq(999), any(EmergencyContactRequest.class)))
                .thenThrow(new EntityNotFoundException("Emergency contact not found"));

        // Act & Assert
        mvc.perform(put(BASE + "/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    /*
     * PRUEBA 4: GET /api/v1/emergency-contacts/customer/{idCustomer} - Búsqueda por ID de cliente
     * Endpoint específico de negocio muy importante
     */
    @Test
    @DisplayName("GET /api/v1/emergency-contacts/customer/{idCustomer} → 200 con contacto del cliente")
    void findByIdCustomer_Ok() throws Exception {
        // Arrange
        EmergencyContactResponse mockContact = createEmergencyContactResponse(1, "María López", "5512345678");
        when(service.findByIdCustomer(1)).thenReturn(mockContact);

        // Act & Assert
        mvc.perform(get(BASE + "/customer/{idCustomer}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Contact identifier:']").value(1))
                .andExpect(jsonPath("$.contactName").value("María López"))
                .andExpect(jsonPath("$.contactPhone").value("5512345678"));
    }

    @Test
    @DisplayName("GET /api/v1/emergency-contacts/customer/{idCustomer} no existente → 404")
    void findByIdCustomer_NotFound() throws Exception {
        // Arrange
        when(service.findByIdCustomer(999)).thenThrow(new EntityNotFoundException("Emergency contact not found for customer"));

        // Act & Assert
        mvc.perform(get(BASE + "/customer/{idCustomer}", 999))
                .andExpect(status().isNotFound());
    }
}