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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerMembershipController.class)
class CustomerMembershipControllerTest {

    @Autowired
    MockMvc mvc;
    
    @Autowired
    ObjectMapper mapper;

    @Autowired
    CustomerMembershipService service;

    private static final String BASE = "/api/v1/customers-memberships";

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
        CustomerMembershipService customerMembershipService() {
            return mock(CustomerMembershipService.class);
        }
    }

    /*
     * ===========================
     * Helpers DTO
     * ===========================
     */
    private CustomerMembershipResponse resp(Long customerId, String customerName, Long membershipId, 
                                           String membershipName, LocalDate startDate, LocalDate endDate, 
                                           LocalDateTime memberSince, Boolean membershipStatus, Long gymId) {
        return CustomerMembershipResponse.builder()
                .customerId(customerId)
                .customerName(customerName)
                .membershipId(membershipId)
                .membershipName(membershipName)
                .startDate(startDate)
                .endDate(endDate)
                .memberSince(memberSince)
                .membershipStatus(membershipStatus)
                .gymId(gymId)
                .build();
    }

    private CustomerMembershipResponse resp(Long customerId, String customerName, Long membershipId, String membershipName) {
        return resp(customerId, customerName, membershipId, membershipName,
                   LocalDate.now(), 
                   LocalDate.now().plusMonths(1), 
                   LocalDateTime.now(), 
                   true, 1L);
    }

    private CustomerMembershipRequest req(Long customerId, Long membershipId, Boolean membershipStatus, 
                                         LocalDate endDate, LocalDateTime memberSince) {
        CustomerMembershipRequest r = new CustomerMembershipRequest();
        r.setCustomerId(customerId);
        r.setMembershipId(membershipId);
        r.setMembershipStatus(membershipStatus);
        r.setEndDate(endDate);
        r.setMemberSince(memberSince);
        return r;
    }

    private CustomerMembershipRequest req(Long customerId, Long membershipId) {
        return req(customerId, membershipId, true, 
                  LocalDate.now().plusMonths(1), 
                  LocalDateTime.now());
    }

    /*
     * ===========================================
     * GET /api/v1/customers-memberships
     * ===========================================
     */

    @Test
    @DisplayName("GET /api/v1/customers-memberships → 200 con lista")
    void findAll_Ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(
            resp(1L, "Ana Pérez", 1L, "Premium Membership"),
            resp(2L, "Luis Sánchez", 2L, "Basic Membership")
        ));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Customer identifier:']").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Ana Pérez"))
                .andExpect(jsonPath("$[1].['Customer identifier:']").value(2));
    }

    @Test
    @DisplayName("GET /api/v1/customers-memberships → 200 con lista vacía")
    void findAll_empty() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * ===================================================
     * GET /api/v1/customers-memberships/customer/{customerId}
     * ===================================================
     */

    @Test
    @DisplayName("GET /customer/{customerId} → 200 con lista")
    void findByCustomerId_Ok() throws Exception {
        when(service.findByCustomerId(1L)).thenReturn(List.of(
            resp(1L, "Ana Pérez", 1L, "Premium Membership"),
            resp(1L, "Ana Pérez", 2L, "Additional Service")
        ));

        mvc.perform(get(BASE + "/customer/{customerId}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[1].customerId").value(1));
    }

    @Test
    @DisplayName("GET /customer/{customerId} → 200 con lista vacía")
    void findByCustomerId_empty() throws Exception {
        when(service.findByCustomerId(999L)).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/customer/{customerId}", 999L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * =====================================================
     * GET /api/v1/customers-memberships/membership/{membershipId}
     * =====================================================
     */

    @Test
    @DisplayName("GET /membership/{membershipId} → 200 con lista")
    void findByMembershipId_Ok() throws Exception {
        when(service.findByMembershipId(1L)).thenReturn(List.of(
            resp(1L, "Ana Pérez", 1L, "Premium Membership"),
            resp(2L, "Luis Sánchez", 1L, "Premium Membership")
        ));

        mvc.perform(get(BASE + "/membership/{membershipId}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].membershipId").value(1))
                .andExpect(jsonPath("$[1].membershipId").value(1));
    }

    /*
     * ================================================
     * GET /api/v1/customers-memberships/gym/{gymId}
     * ================================================
     */

    @Test
    @DisplayName("GET /gym/{gymId} → 200 con lista")
    void findByGymId_Ok() throws Exception {
        when(service.findByGymId(1L)).thenReturn(List.of(
            resp(1L, "Ana Pérez", 1L, "Premium Membership"),
            resp(2L, "Luis Sánchez", 2L, "Basic Membership")
        ));

        mvc.perform(get(BASE + "/gym/{gymId}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].gymId").value(1))
                .andExpect(jsonPath("$[1].gymId").value(1));
    }

    /*
     * ===========================================================
     * GET /api/v1/customers-memberships/{customerId}/{membershipId}
     * ===========================================================
     */

    @Test
    @DisplayName("GET /{customerId}/{membershipId} existente → 200")
    void findById_ok() throws Exception {
        when(service.findById(1L, 1L)).thenReturn(resp(1L, "Ana Pérez", 1L, "Premium Membership"));

        mvc.perform(get(BASE + "/{customerId}/{membershipId}", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Customer identifier:']").value(1))
                .andExpect(jsonPath("$.customerName").value("Ana Pérez"))
                .andExpect(jsonPath("$.membershipId").value(1))
                .andExpect(jsonPath("$.membershipName").value("Premium Membership"));
    }

    @Test
    @DisplayName("GET /{customerId}/{membershipId} no existente → 404")
    void findById_notFound() throws Exception {
        when(service.findById(999L, 999L)).thenThrow(new EntityNotFoundException("Customer membership not found"));

        mvc.perform(get(BASE + "/{customerId}/{membershipId}", 999L, 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /{customerId}/{membershipId} con IDs no numéricos → 400")
    void findById_badPath() throws Exception {
        mvc.perform(get(BASE + "/abc/def"))
                .andExpect(status().isBadRequest());
    }

    /*
     * ===================================
     * POST /api/v1/customers-memberships
     * ===================================
     */

    @Test
    @DisplayName("POST create válido → 201 + Location + body")
    void create_ok() throws Exception {
        CustomerMembershipRequest rq = req(1L, 1L);
        CustomerMembershipResponse created = resp(1L, "Ana Pérez", 1L, "Premium Membership");
        when(service.create(any(CustomerMembershipRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/customers-memberships/1/1"))
                .andExpect(jsonPath("$.['Customer identifier:']").value(1))
                .andExpect(jsonPath("$.customerName").value("Ana Pérez"))
                .andExpect(jsonPath("$.membershipId").value(1))
                .andExpect(jsonPath("$.membershipName").value("Premium Membership"));
    }

    @Test
    @DisplayName("POST create con body inválido → 400")
    void create_invalidBody() throws Exception {
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    /*
     * =========================================================
     * PUT /api/v1/customers-memberships/{customerId}/{membershipId}
     * =========================================================
     */

    @Test
    @DisplayName("PUT update válido → 200 con body actualizado")
    void update_ok() throws Exception {
        CustomerMembershipRequest rq = req(1L, 1L);
        CustomerMembershipResponse updated = resp(1L, "Ana Pérez Actualizada", 1L, "Premium Membership Actualizada");
        when(service.update(eq(1L), eq(1L), any(CustomerMembershipRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{customerId}/{membershipId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Customer identifier:']").value(1))
                .andExpect(jsonPath("$.customerName").value("Ana Pérez Actualizada"))
                .andExpect(jsonPath("$.membershipName").value("Premium Membership Actualizada"));
    }

    @Test
    @DisplayName("PUT update en no existente → 404")
    void update_notFound() throws Exception {
        when(service.update(eq(999L), eq(999L), any(CustomerMembershipRequest.class)))
                .thenThrow(new EntityNotFoundException("Customer membership not found"));

        mvc.perform(put(BASE + "/{customerId}/{membershipId}", 999L, 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req(999L, 999L))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("PUT update con body inválido → 400")
    void update_invalidBody() throws Exception {
        mvc.perform(put(BASE + "/{customerId}/{membershipId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
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
        when(service.findAll()).thenReturn(List.of(resp(1L, "Ana Pérez", 1L, "Premium Membership")));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    /*
     * =====================================
     * Casos especiales
     * =====================================
     */

    @Test
    @DisplayName("GET /{customerId}/{membershipId} con membershipStatus false → 200")
    void findById_withInactiveStatus() throws Exception {
        CustomerMembershipResponse response = resp(1L, "Ana Pérez", 1L, "Expired Membership");
        response.setMembershipStatus(false);
        when(service.findById(1L, 1L)).thenReturn(response);

        mvc.perform(get(BASE + "/{customerId}/{membershipId}", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.membershipStatus").value(false));
    }

    @Test
    @DisplayName("POST create con fechas específicas → 201")
    void create_withSpecificDates() throws Exception {
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        LocalDateTime memberSince = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        CustomerMembershipRequest rq = req(1L, 1L, true, endDate, memberSince);
        CustomerMembershipResponse created = resp(1L, "Ana Pérez", 1L, "Premium Membership");
        created.setEndDate(endDate);
        created.setMemberSince(memberSince);
        
        when(service.create(any(CustomerMembershipRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.endDate").value("2024-12-31"))
                .andExpect(jsonPath("$.memberSince").exists());
    }
}