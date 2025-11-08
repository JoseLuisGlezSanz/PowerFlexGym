package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.EmergencyContactRequest;
import com.example.proyecto.dto.EmergencyContactResponse;
import com.example.proyecto.service.EmergencyContactService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
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

    private static final String BASE = "/api/v1/emergencys-contacts";

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
        EmergencyContactService emergencyContactService() {
            return mock(EmergencyContactService.class);
        }
    }

    /*
     * ===========================
     * Helpers DTO
     * ===========================
     */
    private EmergencyContactResponse resp(Long id, String contactName, String contactPhone) {
        return EmergencyContactResponse.builder()
                .id(id)
                .contactName(contactName)
                .contactPhone(contactPhone)
                .build();
    }

    private EmergencyContactRequest req(String contactName, String contactPhone) {
        EmergencyContactRequest r = new EmergencyContactRequest();
        r.setContactName(contactName);
        r.setContactPhone(contactPhone);
        return r;
    }

    /*
     * ===================================
     * GET /api/v1/emergencys-contacts
     * ===================================
     */

    @Test
    @DisplayName("GET /api/v1/emergencys-contacts → 200 con lista")
    void findAll_Ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(
            resp(1L, "Juan Pérez", "1234567890"),
            resp(2L, "María García", "0987654321")
        ));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Contact identifier:']").value(1))
                .andExpect(jsonPath("$[0].contactName").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].contactPhone").value("1234567890"))
                .andExpect(jsonPath("$[1].['Contact identifier:']").value(2));
    }

    @Test
    @DisplayName("GET /api/v1/emergencys-contacts → 200 con lista vacía")
    void findAll_empty() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * ===================================================
     * GET /api/v1/emergencys-contacts/customer/{customerId}
     * ===================================================
     */

    @Test
    @DisplayName("GET /customer/{customerId} existente → 200")
    void findByIdCustomer_ok() throws Exception {
        when(service.findEmergencyContactByIdCustomer(1L)).thenReturn(resp(1L, "Juan Pérez", "1234567890"));

        mvc.perform(get(BASE + "/customer/{customerId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Contact identifier:']").value(1))
                .andExpect(jsonPath("$.contactName").value("Juan Pérez"))
                .andExpect(jsonPath("$.contactPhone").value("1234567890"));
    }

    @Test
    @DisplayName("GET /customer/{customerId} no existente → 404")
    void findByIdCustomer_notFound() throws Exception {
        when(service.findEmergencyContactByIdCustomer(999L)).thenThrow(new EntityNotFoundException("Emergency contact not found for customer"));

        mvc.perform(get(BASE + "/customer/{customerId}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /customer/{customerId} con ID no numérico → 400")
    void findByIdCustomer_badPath() throws Exception {
        mvc.perform(get(BASE + "/customer/abc"))
                .andExpect(status().isBadRequest());
    }

    /*
     * ===========================================
     * GET /api/v1/emergencys-contacts/{id}
     * ===========================================
     */

    @Test
    @DisplayName("GET /{id} existente → 200")
    void findById_ok() throws Exception {
        when(service.findById(5L)).thenReturn(resp(5L, "Ana López", "5555555555"));

        mvc.perform(get(BASE + "/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Contact identifier:']").value(5))
                .andExpect(jsonPath("$.contactName").value("Ana López"))
                .andExpect(jsonPath("$.contactPhone").value("5555555555"));
    }

    @Test
    @DisplayName("GET /{id} no existente → 404")
    void findById_notFound() throws Exception {
        when(service.findById(999L)).thenThrow(new EntityNotFoundException("Emergency contact not found"));

        mvc.perform(get(BASE + "/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /{id} no numérico → 400 (binding)")
    void findById_badPath() throws Exception {
        mvc.perform(get(BASE + "/abc"))
                .andExpect(status().isBadRequest());
    }

    /*
     * ==================================
     * PUT /api/v1/emergencys-contacts/{id}
     * ==================================
     */

    @Test
    @DisplayName("PUT update válido → 200 con body actualizado")
    void update_ok() throws Exception {
        EmergencyContactRequest rq = req("Nombre Actualizado", "9999999999");
        EmergencyContactResponse updated = resp(10L, "Nombre Actualizado", "9999999999");
        when(service.update(eq(10L), any(EmergencyContactRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Contact identifier:']").value(10))
                .andExpect(jsonPath("$.contactName").value("Nombre Actualizado"))
                .andExpect(jsonPath("$.contactPhone").value("9999999999"));
    }

    @Test
    @DisplayName("PUT update en no existente → 404")
    void update_notFound() throws Exception {
        when(service.update(eq(9999L), any(EmergencyContactRequest.class)))
                .thenThrow(new EntityNotFoundException("Emergency contact not found"));

        mvc.perform(put(BASE + "/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req("X", "0000000000"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("PUT update con body inválido → 400")
    void update_invalidBody() throws Exception {
        mvc.perform(put(BASE + "/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    /*
     * =================================================
     * GET /api/v1/emergencys-contacts/paginationAll
     * =================================================
     */

    @ParameterizedTest(name = "GET /paginationAll?page={0}&pageSize={1} → 200")
    @CsvSource({
            "0,10",
            "1,1",
            "2,50",
            "5,5"
    })
    @DisplayName("GET paginationAll: parámetros válidos")
    void paginationAll_ok(int page, int size) throws Exception {
        when(service.getAll(page, size)).thenReturn(List.of(
            resp(100L, "Eva Martínez", "1111111111"),
            resp(101L, "Carlos Ruiz", "2222222222")
        ));

        mvc.perform(get(BASE + "/paginationAll")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Contact identifier:']").value(100))
                .andExpect(jsonPath("$[0].contactName").value("Eva Martínez"))
                .andExpect(jsonPath("$[1].['Contact identifier:']").value(101));
    }

    @ParameterizedTest(name = "GET /paginationAll?page={0}&pageSize={1} inválidos → 400")
    @CsvSource({
            "-1,10",
            "0,0",
            "0,-5",
            "-3,-3"
    })
    @DisplayName("GET paginationAll: parámetros inválidos → 400")
    void paginationAll_badRequest(int page, int size) throws Exception {
        when(service.getAll(page, size)).thenThrow(new IllegalArgumentException("Invalid paging params"));

        mvc.perform(get(BASE + "/paginationAll")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error", containsStringIgnoringCase("invalid")));
    }

    @Test
    @DisplayName("GET paginationAll sin parámetros → usa valores por defecto")
    void paginationAll_defaultParams() throws Exception {
        when(service.getAll(0, 10)).thenReturn(List.of(resp(1L, "Test Contact", "1234567890")));

        mvc.perform(get(BASE + "/paginationAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    /*
     * =====================================
     * Headers: CORS / Content Negotiation
     * =====================================
     */

    @Test
    @DisplayName("CORS: Access-Control-Allow-Origin")
    void cors_header_present() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE).header("Origin", "https://tu-frontend.com").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "https://tu-frontend.com"));
    }

    @Test
    @DisplayName("Content negotiation: Accept JSON → application/json")
    void contentNegotiation_json() throws Exception {
        when(service.findAll()).thenReturn(List.of(resp(1L, "Test Contact", "1234567890")));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Content negotiation: Accept XML → 406")
    void contentNegotiation_xml() throws Exception {
        mvc.perform(get(BASE).accept(MediaType.APPLICATION_XML))
                .andExpect(status().isNotAcceptable());
    }

    /*
     * =====================================
     * Casos especiales
     * =====================================
     */

    @Test
    @DisplayName("PUT update con campos vacíos → 400")
    void update_emptyFields() throws Exception {
        EmergencyContactRequest rq = req("", "");

        mvc.perform(put(BASE + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET paginationAll sin resultados → 200 con lista vacía")
    void paginationAll_empty() throws Exception {
        when(service.getAll(0, 10)).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/paginationAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /customer/{customerId} con contacto con teléfono null → 200")
    void findByIdCustomer_withNullPhone() throws Exception {
        EmergencyContactResponse response = resp(1L, "Juan Pérez", null);
        when(service.findEmergencyContactByIdCustomer(1L)).thenReturn(response);

        mvc.perform(get(BASE + "/customer/{customerId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contactPhone").isEmpty());
    }
}