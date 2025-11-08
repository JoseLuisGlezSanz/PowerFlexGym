package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.RoleRequest;
import com.example.proyecto.dto.RoleResponse;
import com.example.proyecto.service.RoleService;
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

@WebMvcTest(controllers = RoleController.class)
class RoleControllerTest {

    @Autowired
    MockMvc mvc;
    
    @Autowired
    ObjectMapper mapper;

    @Autowired
    RoleService service;

    private static final String BASE = "/api/v1/roles";

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
        RoleService roleService() {
            return mock(RoleService.class);
        }
    }

    /*
     * ===========================
     * Helpers DTO
     * ===========================
     */
    private RoleResponse resp(Long id, String name, Integer status) {
        return RoleResponse.builder()
                .id(id)
                .name(name)
                .status(status)
                .build();
    }

    private RoleRequest req(String name, Integer status) {
        RoleRequest r = new RoleRequest();
        r.setName(name);
        r.setStatus(status);
        return r;
    }

    /*
     * ================================
     * GET /api/v1/roles
     * ================================
     */

    @Test
    @DisplayName("GET /api/v1/roles → 200 con lista")
    void findAll_Ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(
            resp(1L, "ADMIN", 1),
            resp(2L, "USER", 1),
            resp(3L, "MANAGER", 1)
        ));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].['Role identifier']").value(1))
                .andExpect(jsonPath("$[0].name").value("ADMIN"))
                .andExpect(jsonPath("$[0].status").value(1))
                .andExpect(jsonPath("$[1].['Role identifier']").value(2))
                .andExpect(jsonPath("$[1].name").value("USER"))
                .andExpect(jsonPath("$[2].['Role identifier']").value(3));
    }

    @Test
    @DisplayName("GET /api/v1/roles → 200 con lista vacía")
    void findAll_empty() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * ========================================
     * GET /api/v1/roles/status/{statusValue}
     * ========================================
     */

    @Test
    @DisplayName("GET /status/{statusValue} activo → 200 con lista")
    void findByStatus_active() throws Exception {
        when(service.findByStatus(1)).thenReturn(List.of(
            resp(1L, "ADMIN", 1),
            resp(2L, "USER", 1)
        ));

        mvc.perform(get(BASE + "/status/{statusValue}", 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].status").value(1))
                .andExpect(jsonPath("$[0].name").value("ADMIN"))
                .andExpect(jsonPath("$[1].status").value(1))
                .andExpect(jsonPath("$[1].name").value("USER"));
    }

    @Test
    @DisplayName("GET /status/{statusValue} inactivo → 200 con lista")
    void findByStatus_inactive() throws Exception {
        when(service.findByStatus(0)).thenReturn(List.of(
            resp(4L, "OLD_ROLE", 0)
        ));

        mvc.perform(get(BASE + "/status/{statusValue}", 0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status").value(0))
                .andExpect(jsonPath("$[0].name").value("OLD_ROLE"));
    }

    @Test
    @DisplayName("GET /status/{statusValue} sin resultados → 200 con lista vacía")
    void findByStatus_empty() throws Exception {
        when(service.findByStatus(2)).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/status/{statusValue}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /status/{statusValue} con status no numérico → 400")
    void findByStatus_badPath() throws Exception {
        mvc.perform(get(BASE + "/status/abc"))
                .andExpect(status().isBadRequest());
    }

    /*
     * =================================
     * GET /api/v1/roles/{id}
     * =================================
     */

    @Test
    @DisplayName("GET /{id} existente → 200")
    void findById_ok() throws Exception {
        when(service.findById(5L)).thenReturn(resp(5L, "SUPER_ADMIN", 1));

        mvc.perform(get(BASE + "/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Role identifier']").value(5))
                .andExpect(jsonPath("$.name").value("SUPER_ADMIN"))
                .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    @DisplayName("GET /{id} no existente → 404")
    void findById_notFound() throws Exception {
        when(service.findById(999L)).thenThrow(new EntityNotFoundException("Role not found"));

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
     * POST /api/v1/roles
     * ================================
     */

    @Test
    @DisplayName("POST create válido → 201 + Location + body")
    void create_ok() throws Exception {
        RoleRequest rq = req("MODERATOR", 1);
        RoleResponse created = resp(1234L, "MODERATOR", 1);
        when(service.create(any(RoleRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/roles/1234"))
                .andExpect(jsonPath("$.['Role identifier']").value(1234))
                .andExpect(jsonPath("$.name").value("MODERATOR"))
                .andExpect(jsonPath("$.status").value(1));
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
        RoleRequest rq = req("", 0);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST create con nombre null → 400")
    void create_nullName() throws Exception {
        RoleRequest rq = new RoleRequest();
        rq.setStatus(1);
        // name is null by default

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isBadRequest());
    }

    /*
     * ================================
     * PUT /api/v1/roles/{id}
     * ================================
     */

    @Test
    @DisplayName("PUT update válido → 200 con body actualizado")
    void update_ok() throws Exception {
        RoleRequest rq = req("ADMIN_UPDATED", 1);
        RoleResponse updated = resp(10L, "ADMIN_UPDATED", 1);
        when(service.update(eq(10L), any(RoleRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Role identifier']").value(10))
                .andExpect(jsonPath("$.name").value("ADMIN_UPDATED"))
                .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    @DisplayName("PUT update con status inactivo → 200")
    void update_inactiveStatus() throws Exception {
        RoleRequest rq = req("DISABLED_ROLE", 0);
        RoleResponse updated = resp(15L, "DISABLED_ROLE", 0);
        when(service.update(eq(15L), any(RoleRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 15L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(0));
    }

    @Test
    @DisplayName("PUT update en no existente → 404")
    void update_notFound() throws Exception {
        when(service.update(eq(9999L), any(RoleRequest.class)))
                .thenThrow(new EntityNotFoundException("Role not found"));

        mvc.perform(put(BASE + "/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req("TEST_ROLE", 1))))
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
        when(service.findAll()).thenReturn(List.of(resp(1L, "TEST_ROLE", 1)));

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
    @DisplayName("GET /status/{statusValue} con status negativo → 200 (si el servicio lo permite)")
    void findByStatus_negative() throws Exception {
        when(service.findByStatus(-1)).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/status/{statusValue}", -1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("POST create con nombre en minúsculas → 201")
    void create_lowercaseName() throws Exception {
        RoleRequest rq = req("viewer", 1);
        RoleResponse created = resp(100L, "viewer", 1);
        when(service.create(any(RoleRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("viewer"));
    }

    @Test
    @DisplayName("PUT update sin cambios → 200")
    void update_sameData() throws Exception {
        RoleRequest rq = req("USER", 1);
        RoleResponse updated = resp(2L, "USER", 1);
        when(service.update(eq(2L), any(RoleRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("USER"));
    }

    @Test
    @DisplayName("GET /status/{statusValue} con múltiples roles → 200")
    void findByStatus_multipleRoles() throws Exception {
        when(service.findByStatus(1)).thenReturn(List.of(
            resp(1L, "ADMIN", 1),
            resp(2L, "USER", 1),
            resp(3L, "MANAGER", 1),
            resp(4L, "MODERATOR", 1)
        ));

        mvc.perform(get(BASE + "/status/{statusValue}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].status").value(1))
                .andExpect(jsonPath("$[1].status").value(1))
                .andExpect(jsonPath("$[2].status").value(1))
                .andExpect(jsonPath("$[3].status").value(1));
    }

    @Test
    @DisplayName("POST create con nombre con espacios → 400 (depende de validación)")
    void create_nameWithSpaces() throws Exception {
        RoleRequest rq = req("ROLE WITH SPACES", 1);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isBadRequest());
    }
}