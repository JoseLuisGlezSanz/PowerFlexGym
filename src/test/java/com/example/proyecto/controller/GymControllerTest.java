package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.GymRequest;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.service.GymService;
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

@WebMvcTest(controllers = GymController.class)
class GymControllerTest {

    @Autowired
    MockMvc mvc;
    
    @Autowired
    ObjectMapper mapper;

    @Autowired
    GymService service;

    private static final String BASE = "/api/v1/gyms";

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
        GymService gymService() {
            return mock(GymService.class);
        }
    }

    /*
     * ===========================
     * Helpers DTO
     * ===========================
     */
    private GymResponse resp(Long id, String name) {
        return GymResponse.builder()
                .id(id)
                .name(name)
                .build();
    }

    private GymRequest req(String name) {
        GymRequest r = new GymRequest();
        r.setName(name);
        return r;
    }

    /*
     * ===========================
     * GET /api/v1/gyms
     * ===========================
     */

    @Test
    @DisplayName("GET /api/v1/gyms → 200 con lista")
    void findAll_Ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(
            resp(1L, "Power Gym"),
            resp(2L, "Fitness Center")
        ));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Gym identifier:']").value(1))
                .andExpect(jsonPath("$[0].name").value("Power Gym"))
                .andExpect(jsonPath("$[1].['Gym identifier:']").value(2))
                .andExpect(jsonPath("$[1].name").value("Fitness Center"));
    }

    @Test
    @DisplayName("GET /api/v1/gyms → 200 con lista vacía")
    void findAll_empty() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * ====================================
     * GET /api/v1/gyms/name/{name}
     * ====================================
     */

    @Test
    @DisplayName("GET /name/{name} existente → 200")
    void findByNameGym_ok() throws Exception {
        when(service.findByName("Power Gym")).thenReturn(resp(1L, "Power Gym"));

        mvc.perform(get(BASE + "/name/{name}", "Power Gym"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Gym identifier:']").value(1))
                .andExpect(jsonPath("$.name").value("Power Gym"));
    }

    @Test
    @DisplayName("GET /name/{name} no existente → 404")
    void findByNameGym_notFound() throws Exception {
        when(service.findByName("NonExistent Gym")).thenThrow(new EntityNotFoundException("Gym not found"));

        mvc.perform(get(BASE + "/name/{name}", "NonExistent Gym"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    /*
     * =================================
     * GET /api/v1/gyms/{id}
     * =================================
     */

    @Test
    @DisplayName("GET /{id} existente → 200")
    void findById_ok() throws Exception {
        when(service.findById(5L)).thenReturn(resp(5L, "Elite Fitness"));

        mvc.perform(get(BASE + "/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Gym identifier:']").value(5))
                .andExpect(jsonPath("$.name").value("Elite Fitness"));
    }

    @Test
    @DisplayName("GET /{id} no existente → 404")
    void findById_notFound() throws Exception {
        when(service.findById(999L)).thenThrow(new EntityNotFoundException("Gym not found"));

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
     * ============================
     * POST /api/v1/gyms
     * ============================
     */

    @Test
    @DisplayName("POST create válido → 201 + Location + body")
    void create_ok() throws Exception {
        GymRequest rq = req("New Gym");
        GymResponse created = resp(1234L, "New Gym");
        when(service.create(any(GymRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/gyms/1234"))
                .andExpect(jsonPath("$.['Gym identifier:']").value(1234))
                .andExpect(jsonPath("$.name").value("New Gym"));
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
    @DisplayName("POST create con nombre vacío → 400")
    void create_emptyName() throws Exception {
        GymRequest rq = req("");

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isBadRequest());
    }

    /*
     * ============================
     * PUT /api/v1/gyms/{id}
     * ============================
     */

    @Test
    @DisplayName("PUT update válido → 200 con body actualizado")
    void update_ok() throws Exception {
        GymRequest rq = req("Gym Actualizado");
        GymResponse updated = resp(10L, "Gym Actualizado");
        when(service.update(eq(10L), any(GymRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Gym identifier:']").value(10))
                .andExpect(jsonPath("$.name").value("Gym Actualizado"));
    }

    @Test
    @DisplayName("PUT update en no existente → 404")
    void update_notFound() throws Exception {
        when(service.update(eq(9999L), any(GymRequest.class)))
                .thenThrow(new EntityNotFoundException("Gym not found"));

        mvc.perform(put(BASE + "/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req("Gym Name"))))
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
     * ======================================
     * GET /api/v1/gyms/paginationAll
     * ======================================
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
            resp(100L, "Gym Alpha"),
            resp(101L, "Gym Beta")
        ));

        mvc.perform(get(BASE + "/paginationAll")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['Gym identifier:']").value(100))
                .andExpect(jsonPath("$[0].name").value("Gym Alpha"))
                .andExpect(jsonPath("$[1].['Gym identifier:']").value(101));
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
        when(service.getAll(0, 10)).thenReturn(List.of(resp(1L, "Default Gym")));

        mvc.perform(get(BASE + "/paginationAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Default Gym"));
    }

    @Test
    @DisplayName("GET paginationAll sin resultados → 200 con lista vacía")
    void paginationAll_empty() throws Exception {
        when(service.getAll(0, 10)).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/paginationAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
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
        when(service.findAll()).thenReturn(List.of(resp(1L, "Test Gym")));

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
    void findByNameGym_withSpaces() throws Exception {
        when(service.findByName("Premium Fitness Center")).thenReturn(resp(3L, "Premium Fitness Center"));

        mvc.perform(get(BASE + "/name/{name}", "Premium Fitness Center"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Premium Fitness Center"));
    }

    @Test
    @DisplayName("POST create con nombre muy largo → 400")
    void create_longName() throws Exception {
        String longName = "A".repeat(256);
        GymRequest rq = req(longName);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT update sin cambios → 200")
    void update_sameData() throws Exception {
        GymRequest rq = req("Same Name");
        GymResponse updated = resp(15L, "Same Name");
        when(service.update(eq(15L), any(GymRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 15L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Same Name"));
    }
}