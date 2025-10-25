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

import static org.hamcrest.Matchers.hasSize;
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

    @TestConfiguration
    static class TestConfig {
        @Bean
        RoleService roleService() {
            return mock(RoleService.class);
        }
    }

    // Helpers para crear DTOs de prueba
    private RoleResponse createRoleResponse(Integer id, String roleName, Integer status) {
        return RoleResponse.builder()
                .idRole(id)
                .role(roleName)
                .status(status)
                .build();
    }

    private RoleRequest createRoleRequest(String roleName, Integer status) {
        RoleRequest request = new RoleRequest();
        request.setRole(roleName);
        request.setStatus(status);
        return request;
    }

    /*
     * PRUEBA 1: GET /api/v1/roles - Obtener todos los roles
     */
    @Test
    @DisplayName("GET /api/v1/roles → 200 con lista de roles")
    void findAll_Ok() throws Exception {
        // Arrange
        List<RoleResponse> mockRoles = List.of(
            createRoleResponse(1, "Administrador", 1),
            createRoleResponse(2, "Entrenador", 1),
            createRoleResponse(3, "Recepcionista", 1)
        );
        when(service.findAll()).thenReturn(mockRoles);

        // Act & Assert
        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].['Role identifier']").value(1))
                .andExpect(jsonPath("$[0].role").value("Administrador"))
                .andExpect(jsonPath("$[0].status").value(1))
                .andExpect(jsonPath("$[1].['Role identifier']").value(2))
                .andExpect(jsonPath("$[1].role").value("Entrenador"))
                .andExpect(jsonPath("$[1].status").value(1))
                .andExpect(jsonPath("$[2].['Role identifier']").value(3))
                .andExpect(jsonPath("$[2].role").value("Recepcionista"))
                .andExpect(jsonPath("$[2].status").value(1));
    }

    @Test
    @DisplayName("GET /api/v1/roles → 200 con lista vacía")
    void findAll_Empty() throws Exception {
        // Arrange
        when(service.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * PRUEBA 2: GET /api/v1/roles/{id} - Obtener rol por ID
     */
    @Test
    @DisplayName("GET /api/v1/roles/{id} existente → 200")
    void findById_Ok() throws Exception {
        // Arrange
        RoleResponse mockRole = createRoleResponse(1, "Administrador", 1);
        when(service.findById(1)).thenReturn(mockRole);

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Role identifier']").value(1))
                .andExpect(jsonPath("$.role").value("Administrador"))
                .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    @DisplayName("GET /api/v1/roles/{id} no existente → 404")
    void findById_NotFound() throws Exception {
        // Arrange
        when(service.findById(999)).thenThrow(new EntityNotFoundException("Role not found"));

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
    }

    /*
     * PRUEBA 3: POST /api/v1/roles - Crear nuevo rol
     */
    @Test
    @DisplayName("POST /api/v1/roles → 201 creado exitosamente")
    void create_Ok() throws Exception {
        // Arrange
        RoleRequest request = createRoleRequest("Nuevo Rol", 1);
        RoleResponse response = createRoleResponse(123, "Nuevo Rol", 1);
        
        when(service.save(any(RoleRequest.class))).thenReturn(response);

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.['Role identifier']").value(123))
                .andExpect(jsonPath("$.role").value("Nuevo Rol"))
                .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    @DisplayName("POST /api/v1/roles con datos inválidos → 400")
    void create_InvalidBody() throws Exception {
        // Arrange - Body sin campos requeridos
        RoleRequest invalidRequest = new RoleRequest(); // Todos los campos null

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    /*
     * PRUEBA 4: PUT /api/v1/roles/{id} - Actualizar rol existente
     * Importante para la gestión de permisos y estados de roles
     */
    @Test
    @DisplayName("PUT /api/v1/roles/{id} → 200 actualizado exitosamente")
    void update_Ok() throws Exception {
        // Arrange
        RoleRequest request = createRoleRequest("Administrador Actualizado", 0);
        RoleResponse response = createRoleResponse(1, "Administrador Actualizado", 0);
        
        when(service.update(eq(1), any(RoleRequest.class))).thenReturn(response);

        // Act & Assert
        mvc.perform(put(BASE + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Role identifier']").value(1))
                .andExpect(jsonPath("$.role").value("Administrador Actualizado"))
                .andExpect(jsonPath("$.status").value(0));
    }

    @Test
    @DisplayName("PUT /api/v1/roles/{id} no existente → 404")
    void update_NotFound() throws Exception {
        // Arrange
        RoleRequest request = createRoleRequest("Rol Actualizado", 1);
        when(service.update(eq(999), any(RoleRequest.class)))
                .thenThrow(new EntityNotFoundException("Role not found"));

        // Act & Assert
        mvc.perform(put(BASE + "/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}