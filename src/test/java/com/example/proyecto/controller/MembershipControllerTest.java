package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.MembershipRequest;
import com.example.proyecto.dto.MembershipResponse;
import com.example.proyecto.service.MembershipService;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MembershipController.class)
class MembershipControllerTest {

    @Autowired
    MockMvc mvc;
    
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MembershipService service;

    private static final String BASE = "/api/v1/memberships";

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
        MembershipService membershipService() {
            return mock(MembershipService.class);
        }
    }

    /*
     * ===========================
     * Helpers DTO
     * ===========================
     */
    private MembershipResponse resp(Long id, String name, String duration, Double price, Integer status, Long gymId) {
        return MembershipResponse.builder()
                .id(id)
                .name(name)
                .duration(duration)
                .price(price)
                .status(status)
                .gymId(gymId)
                .build();
    }

    private MembershipResponse resp(Long id, String name, String duration, Double price) {
        return resp(id, name, duration, price, 1, 1L);
    }

    private MembershipRequest req(String name, String duration, Double price, Integer status, Long gymId) {
        MembershipRequest r = new MembershipRequest();
        r.setName(name);
        r.setDuration(duration);
        r.setPrice(price);
        r.setStatus(status);
        r.setGymId(gymId);
        return r;
    }

    private MembershipRequest req(String name, String duration, Double price) {
        return req(name, duration, price, 1, 1L);
    }

    /*
     * ================================
     * GET /api/v1/memberships
     * ================================
     */

    @Test
    @DisplayName("GET /api/v1/memberships → 200 con lista")
    void findAll_Ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(
            resp(1L, "Premium", "1 month", 99.99),
            resp(2L, "Basic", "3 months", 199.99)
        ));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Membership identifier:']").value(1))
                .andExpect(jsonPath("$[0].name").value("Premium"))
                .andExpect(jsonPath("$[0].duration").value("1 month"))
                .andExpect(jsonPath("$[0].price").value(99.99))
                .andExpect(jsonPath("$[1].['Membership identifier:']").value(2));
    }

    @Test
    @DisplayName("GET /api/v1/memberships → 200 con lista vacía")
    void findAll_empty() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * ========================================
     * GET /api/v1/memberships/name/{name}
     * ========================================
     */

    @Test
    @DisplayName("GET /name/{name} → 200 con lista")
    void findMembershipByName_Ok() throws Exception {
        when(service.findMembershipByName("Premium")).thenReturn(List.of(
            resp(1L, "Premium", "1 month", 99.99),
            resp(3L, "Premium Plus", "1 month", 149.99)
        ));

        mvc.perform(get(BASE + "/name/{name}", "Premium").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Premium"))
                .andExpect(jsonPath("$[1].name").value("Premium Plus"));
    }

    @Test
    @DisplayName("GET /name/{name} sin resultados → 200 con lista vacía")
    void findMembershipByName_empty() throws Exception {
        when(service.findMembershipByName("NonExistent")).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/name/{name}", "NonExistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * ========================================
     * GET /api/v1/memberships/gym/{gymId}
     * ========================================
     */

    @Test
    @DisplayName("GET /gym/{gymId} → 200 con lista")
    void findMembershipByGymId_Ok() throws Exception {
        when(service.findMembershipByGymId(1L)).thenReturn(List.of(
            resp(1L, "Premium", "1 month", 99.99, 1, 1L),
            resp(2L, "Basic", "3 months", 199.99, 1, 1L)
        ));

        mvc.perform(get(BASE + "/gym/{gymId}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].gymId").value(1))
                .andExpect(jsonPath("$[1].gymId").value(1));
    }

    @Test
    @DisplayName("GET /gym/{gymId} sin resultados → 200 con lista vacía")
    void findMembershipByGymId_empty() throws Exception {
        when(service.findMembershipByGymId(999L)).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/gym/{gymId}", 999L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * =================================
     * GET /api/v1/memberships/{id}
     * =================================
     */

    @Test
    @DisplayName("GET /{id} existente → 200")
    void findById_ok() throws Exception {
        when(service.findById(5L)).thenReturn(resp(5L, "Gold Membership", "6 months", 499.99, 1, 2L));

        mvc.perform(get(BASE + "/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Membership identifier:']").value(5))
                .andExpect(jsonPath("$.name").value("Gold Membership"))
                .andExpect(jsonPath("$.duration").value("6 months"))
                .andExpect(jsonPath("$.price").value(499.99))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.gymId").value(2));
    }

    @Test
    @DisplayName("GET /{id} no existente → 404")
    void findById_notFound() throws Exception {
        when(service.findById(999L)).thenThrow(new EntityNotFoundException("Membership not found"));

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
     * ================================
     * POST /api/v1/memberships
     * ================================
     */

    @Test
    @DisplayName("POST create válido → 201 + Location + body")
    void create_ok() throws Exception {
        MembershipRequest rq = req("New Membership", "2 months", 149.99, 1, 1L);
        MembershipResponse created = resp(1234L, "New Membership", "2 months", 149.99, 1, 1L);
        when(service.create(any(MembershipRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/memberships/1234"))
                .andExpect(jsonPath("$.['Membership identifier:']").value(1234))
                .andExpect(jsonPath("$.name").value("New Membership"))
                .andExpect(jsonPath("$.duration").value("2 months"))
                .andExpect(jsonPath("$.price").value(149.99))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.gymId").value(1));
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

    @Test
    @DisplayName("POST create con campos vacíos → 400")
    void create_emptyFields() throws Exception {
        MembershipRequest rq = req("", "", 0.0, 0, null);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isBadRequest());
    }

    /*
     * ================================
     * PUT /api/v1/memberships/{id}
     * ================================
     */

    @Test
    @DisplayName("PUT update válido → 200 con body actualizado")
    void update_ok() throws Exception {
        MembershipRequest rq = req("Updated Membership", "12 months", 899.99, 1, 2L);
        MembershipResponse updated = resp(10L, "Updated Membership", "12 months", 899.99, 1, 2L);
        when(service.update(eq(10L), any(MembershipRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Membership identifier:']").value(10))
                .andExpect(jsonPath("$.name").value("Updated Membership"))
                .andExpect(jsonPath("$.duration").value("12 months"))
                .andExpect(jsonPath("$.price").value(899.99))
                .andExpect(jsonPath("$.gymId").value(2));
    }

    @Test
    @DisplayName("PUT update en no existente → 404")
    void update_notFound() throws Exception {
        when(service.update(eq(9999L), any(MembershipRequest.class)))
                .thenThrow(new EntityNotFoundException("Membership not found"));

        mvc.perform(put(BASE + "/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req("Test", "1 month", 99.99))))
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
        when(service.findAll()).thenReturn(List.of(resp(1L, "Test Membership", "1 month", 99.99)));

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
    @DisplayName("GET /name/{name} con nombre con espacios → 200")
    void findMembershipByName_withSpaces() throws Exception {
        when(service.findMembershipByName("Family Package")).thenReturn(List.of(
            resp(4L, "Family Package", "3 months", 299.99)
        ));

        mvc.perform(get(BASE + "/name/{name}", "Family Package"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Family Package"));
    }

    @Test
    @DisplayName("POST create con precio cero → 201")
    void create_withZeroPrice() throws Exception {
        MembershipRequest rq = req("Free Trial", "1 week", 0.0, 1, 1L);
        MembershipResponse created = resp(100L, "Free Trial", "1 week", 0.0, 1, 1L);
        when(service.create(any(MembershipRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price").value(0.0));
    }

    @Test
    @DisplayName("PUT update con status inactivo → 200")
    void update_withInactiveStatus() throws Exception {
        MembershipRequest rq = req("Inactive Membership", "1 month", 99.99, 0, 1L);
        MembershipResponse updated = resp(20L, "Inactive Membership", "1 month", 99.99, 0, 1L);
        when(service.update(eq(20L), any(MembershipRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 20L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(0));
    }

    @Test
    @DisplayName("GET /gym/{gymId} con múltiples membresías → 200")
    void findMembershipByGymId_multiple() throws Exception {
        when(service.findMembershipByGymId(3L)).thenReturn(List.of(
            resp(1L, "Basic", "1 month", 49.99, 1, 3L),
            resp(2L, "Premium", "1 month", 79.99, 1, 3L),
            resp(3L, "VIP", "1 month", 129.99, 1, 3L)
        ));

        mvc.perform(get(BASE + "/gym/{gymId}", 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].gymId").value(3))
                .andExpect(jsonPath("$[1].gymId").value(3))
                .andExpect(jsonPath("$[2].gymId").value(3));
    }

    @Test
    @DisplayName("POST create con precio negativo → 400")
    void create_withNegativePrice() throws Exception {
        MembershipRequest rq = req("Invalid Membership", "1 month", -50.0, 1, 1L);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isBadRequest());
    }
}