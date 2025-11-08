package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.CustomerRequest;
import com.example.proyecto.dto.CustomerResponse;
import com.example.proyecto.service.CustomerService;
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

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.hasSize;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private CustomerResponse resp(Long id, String name, String cologne, String phone, Date birthDate, Boolean medicalCondition, 
            String photo, String photoCredential, Boolean verifiedNumber, Long gymId, Long contactId, LocalDateTime registrationDate) {
        return CustomerResponse.builder()
                .id(id)
                .name(name)
                .cologne(cologne)
                .phone(phone)
                .birthDate(birthDate)
                .medicalCondition(medicalCondition)
                .photo(photo)
                .photoCredential(photoCredential)
                .verifiedNumber(verifiedNumber)
                .gymId(gymId)
                .contactId(contactId)
                .registrationDate(registrationDate)
                .build();
    }

    private CustomerResponse resp(Long id, String name, String cologne) {
        return resp(id, name, cologne, "1234567890", 
                   Date.valueOf(LocalDate.of(1990, 1, 1)), false, 
                   "photo.jpg", "credential.jpg", true, 1L, 1L, 
                   LocalDateTime.now());
    }

    private CustomerRequest req(String name, String cologne, String phone, Date birthDate, Boolean medicalCondition, 
            String photo, String photoCredential, Boolean verifiedNumber, Long gymId, String contactName, String contactPhone) {
        CustomerRequest r = new CustomerRequest();
        r.setName(name);
        r.setCologne(cologne);
        r.setPhone(phone);
        r.setBirthDate(birthDate);
        r.setMedicalCondition(medicalCondition);
        r.setPhoto(photo);
        r.setPhotoCredential(photoCredential);
        r.setVerifiedNumber(verifiedNumber);
        r.setGymId(gymId);
        r.setContactName(contactName);
        r.setContactPhone(contactPhone);
        return r;
    }

    private CustomerRequest req(String name, String cologne) {
        return req(name, cologne, "1234567890", 
                  Date.valueOf(LocalDate.of(1990, 1, 1)), false, 
                  "photo.jpg", "credential.jpg", true, 1L, 
                  "Contact Name", "0987654321");
    }

    /*
     * ===========================
     * GET /api/v1/customers
     * ===========================
     */

    @Test
    @DisplayName("GET /api/v1/customers → 200 con lista")
    void findAll_Ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(
            resp(1L, "Ana", "Pérez"), 
            resp(2L, "Luis", "Sánchez")
        ));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Customer identifier:']").value(1))
                .andExpect(jsonPath("$[0].name").value("Ana"))
                .andExpect(jsonPath("$[1].['Customer identifier:']").value(2));
    }

    @Test
    @DisplayName("GET /api/v1/customers → 200 con lista vacía")
    void findAll_empty() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * ==========================================
     * GET /api/v1/customers/gym/{gymId}
     * ==========================================
     */

    @Test
    @DisplayName("GET /gym/{gymId} → 200 con lista")
    void findByGymId_Ok() throws Exception {
        when(service.findAllCustomersByGymId(1L)).thenReturn(List.of(
            resp(1L, "Ana", "Pérez"), 
            resp(2L, "Luis", "Sánchez")
        ));

        mvc.perform(get(BASE + "/gym/{gymId}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].gymId").value(1))
                .andExpect(jsonPath("$[1].gymId").value(1));
    }

    /*
     * ==========================================
     * GET /api/v1/customers/name/{name}
     * ==========================================
     */

    @Test
    @DisplayName("GET /name/{name} → 200 con lista")
    void findByName_Ok() throws Exception {
        when(service.findByName("Ana")).thenReturn(List.of(
            resp(1L, "Ana", "Pérez"), 
            resp(3L, "Anabel", "García")
        ));

        mvc.perform(get(BASE + "/name/{name}", "Ana").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Ana"))
                .andExpect(jsonPath("$[1].name").value("Anabel"));
    }

    /*
     * ==========================================
     * GET /api/v1/customers/verifiedNumber/{verifiedNumber}
     * ==========================================
     */

    @Test
    @DisplayName("GET /verifiedNumber/{verifiedNumber} → 200 con lista")
    void findByVerifiedNumberTrue_Ok() throws Exception {
        when(service.findByVerifiedNumberTrue()).thenReturn(List.of(
            resp(1L, "Ana", "Pérez"), 
            resp(2L, "Luis", "Sánchez")
        ));

        mvc.perform(get(BASE + "/verifiedNumber/{verifiedNumber}", true).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].verifiedNumber").value(true))
                .andExpect(jsonPath("$[1].verifiedNumber").value(true));
    }

    /*
     * ======================================
     * GET /api/v1/customers/{id}
     * ======================================
     */

    @Test
    @DisplayName("GET /{id} existente → 200")
    void findById_ok() throws Exception {
        when(service.findById(7L)).thenReturn(resp(7L, "María", "Pérez"));

        mvc.perform(get(BASE + "/{id}", 7L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Customer identifier:']").value(7))
                .andExpect(jsonPath("$.name").value("María"));
    }

    @Test
    @DisplayName("GET /{id} no existente → 404")
    void findById_notFound() throws Exception {
        when(service.findById(999L)).thenThrow(new EntityNotFoundException("Customer not found"));

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
     * ==============================
     * POST /api/v1/customers
     * ==============================
     */

    @Test
    @DisplayName("POST create válido → 201 + Location + body")
    void create_ok() throws Exception {
        CustomerRequest rq = req("María", "Pérez");
        CustomerResponse created = resp(1234L, "María", "Pérez");
        when(service.create(any(CustomerRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/customers/1234"))
                .andExpect(jsonPath("$.['Customer identifier:']").value(1234))
                .andExpect(jsonPath("$.name").value("María"))
                .andExpect(jsonPath("$.cologne").value("Pérez"));
    }

    /*
     * ==================================
     * PUT /api/v1/customers/{id}
     * ==================================
     */

    @Test
    @DisplayName("PUT update válido → 200 con body actualizado")
    void update_ok() throws Exception {
        CustomerRequest rq = req("Nombre Editado", "Pérez");
        CustomerResponse updated = resp(55L, "Nombre Editado", "Pérez");
        when(service.update(eq(55L), any(CustomerRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 55L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Customer identifier:']").value(55))
                .andExpect(jsonPath("$.name").value("Nombre Editado"));
    }

    @Test
    @DisplayName("PUT update en no existente → 404")
    void update_notFound() throws Exception {
        when(service.update(eq(9999L), any(CustomerRequest.class)))
                .thenThrow(new EntityNotFoundException("Customer not found"));

        mvc.perform(put(BASE + "/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req("X", "Pérez"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    /*
     * ==========================================
     * GET /api/v1/customers/paginationAll
     * ==========================================
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
        when(service.getAll(page, size)).thenReturn(List.of(resp(100L, "Eva", "Pérez")));

        mvc.perform(get(BASE + "/paginationAll")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].['Customer identifier:']").value(100))
                .andExpect(jsonPath("$[0].name").value("Eva"));
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

    /*
     * ==========================================
     * GET /api/v1/customers/paginationByVerifiedNumberTrue
     * ==========================================
     */

    @Test
    @DisplayName("GET paginationByVerifiedNumberTrue → 200")
    void getByVerifiedNumberTruePaginated_ok() throws Exception {
        when(service.getByVerifiedNumberTrue(0, 10)).thenReturn(List.of(
            resp(1L, "Ana", "Pérez"), 
            resp(2L, "Luis", "Sánchez")
        ));

        mvc.perform(get(BASE + "/paginationByVerifiedNumberTrue")
                .queryParam("page", "0")
                .queryParam("pageSize", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].verifiedNumber").value(true));
    }

    /*
     * ==========================================
     * GET /api/v1/customers/paginationByGymId
     * ==========================================
     */

    @Test
    @DisplayName("GET paginationByGymId → 200")
    void getByGymIdPaginated_ok() throws Exception {
        when(service.getAllCustomersByGymId(0, 10, 1L)).thenReturn(List.of(
            resp(1L, "Ana", "Pérez"), 
            resp(2L, "Luis", "Sánchez")
        ));

        mvc.perform(get(BASE + "/paginationByGymId")
                .queryParam("page", "0")
                .queryParam("pageSize", "10")
                .queryParam("gymId", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].gymId").value(1));
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
        when(service.findAll()).thenReturn(List.of(resp(1L, "A", "Pérez")));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}