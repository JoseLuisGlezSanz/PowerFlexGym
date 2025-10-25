package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.UserRequest;
import com.example.proyecto.dto.UserResponse;
import com.example.proyecto.dto.RoleResponse;
import com.example.proyecto.dto.GymResponse;
import com.example.proyecto.service.UserService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mvc;
    
    @Autowired
    ObjectMapper mapper;

    @Autowired
    UserService service;

    private static final String BASE = "/api/v1/users";

    @BeforeEach
    void beforeEach() {
        reset(service);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        UserService userService() {
            return mock(UserService.class);
        }
    }

    // Helpers para crear DTOs de prueba
    private UserResponse createUserResponse(Integer id, String username, String email, String name, 
                                          String roleName, String gymName) {
        return UserResponse.builder()
                .idUser(id)
                .user(username)
                .mail(email)
                .phone("5512345678")
                .name(name)
                .password("encryptedPassword")
                .status(1)
                .role(RoleResponse.builder()
                        .idRole(1)
                        .role(roleName)
                        .status(1)
                        .build())
                .gym(GymResponse.builder()
                        .idGym(1)
                        .gym(gymName)
                        .build())
                .build();
    }

    private UserRequest createUserRequest(String username, String email, String name, 
                                        Integer idRole, Integer idGym) {
        UserRequest request = new UserRequest();
        request.setUser(username);
        request.setMail(email);
        request.setPhone("5512345678");
        request.setName(name);
        request.setPassword("plainPassword");
        request.setStatus(1);
        request.setIdRole(idRole);
        request.setIdGym(idGym);
        return request;
    }

    /*
     * PRUEBA 1: GET /api/v1/users - Obtener todos los usuarios
     */
    @Test
    @DisplayName("GET /api/v1/users → 200 con lista de usuarios")
    void findAll_Ok() throws Exception {
        // Arrange
        List<UserResponse> mockUsers = List.of(
            createUserResponse(1, "admin1", "admin1@gym.com", "Juan Admin", "Administrador", "Gym Central"),
            createUserResponse(2, "trainer1", "trainer1@gym.com", "María Entrenadora", "Entrenador", "Gym Norte"),
            createUserResponse(3, "recep1", "recep1@gym.com", "Carlos Recepcionista", "Recepcionista", "Gym Sur")
        );
        when(service.findAll()).thenReturn(mockUsers);

        // Act & Assert
        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].['User identifier']").value(1))
                .andExpect(jsonPath("$[0].user").value("admin1"))
                .andExpect(jsonPath("$[0].mail").value("admin1@gym.com"))
                .andExpect(jsonPath("$[0].name").value("Juan Admin"))
                .andExpect(jsonPath("$[0].status").value(1))
                .andExpect(jsonPath("$[0].role.role").value("Administrador"))
                .andExpect(jsonPath("$[0].gym.gym").value("Gym Central"))
                .andExpect(jsonPath("$[1].['User identifier']").value(2))
                .andExpect(jsonPath("$[1].user").value("trainer1"))
                .andExpect(jsonPath("$[1].name").value("María Entrenadora"))
                .andExpect(jsonPath("$[1].role.role").value("Entrenador"))
                .andExpect(jsonPath("$[2].['User identifier']").value(3))
                .andExpect(jsonPath("$[2].user").value("recep1"))
                .andExpect(jsonPath("$[2].name").value("Carlos Recepcionista"))
                .andExpect(jsonPath("$[2].role.role").value("Recepcionista"));
    }

    @Test
    @DisplayName("GET /api/v1/users → 200 con lista vacía")
    void findAll_Empty() throws Exception {
        // Arrange
        when(service.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * PRUEBA 2: GET /api/v1/users/{id} - Obtener usuario por ID
     */
    @Test
    @DisplayName("GET /api/v1/users/{id} existente → 200")
    void findById_Ok() throws Exception {
        // Arrange
        UserResponse mockUser = createUserResponse(1, "admin1", "admin1@gym.com", "Juan Admin", "Administrador", "Gym Central");
        when(service.findById(1)).thenReturn(mockUser);

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['User identifier']").value(1))
                .andExpect(jsonPath("$.user").value("admin1"))
                .andExpect(jsonPath("$.mail").value("admin1@gym.com"))
                .andExpect(jsonPath("$.name").value("Juan Admin"))
                .andExpect(jsonPath("$.phone").value("5512345678"))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.role.role").value("Administrador"))
                .andExpect(jsonPath("$.gym.gym").value("Gym Central"));
    }

    @Test
    @DisplayName("GET /api/v1/users/{id} no existente → 404")
    void findById_NotFound() throws Exception {
        // Arrange
        when(service.findById(999)).thenThrow(new EntityNotFoundException("User not found"));

        // Act & Assert
        mvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
    }

    /*
     * PRUEBA 3: POST /api/v1/users - Crear nuevo usuario
     */
    @Test
    @DisplayName("POST /api/v1/users → 201 creado exitosamente")
    void create_Ok() throws Exception {
        // Arrange
        UserRequest request = createUserRequest("newuser", "newuser@gym.com", "Nuevo Usuario", 1, 1);
        UserResponse response = createUserResponse(123, "newuser", "newuser@gym.com", "Nuevo Usuario", "Administrador", "Gym Central");
        
        when(service.save(any(UserRequest.class))).thenReturn(response);

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.['User identifier']").value(123))
                .andExpect(jsonPath("$.user").value("newuser"))
                .andExpect(jsonPath("$.mail").value("newuser@gym.com"))
                .andExpect(jsonPath("$.name").value("Nuevo Usuario"))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.role.role").value("Administrador"))
                .andExpect(jsonPath("$.gym.gym").value("Gym Central"));
    }

    @Test
    @DisplayName("POST /api/v1/users con datos inválidos → 400")
    void create_InvalidBody() throws Exception {
        // Arrange - Body sin campos requeridos
        UserRequest invalidRequest = new UserRequest(); // Todos los campos null

        // Act & Assert
        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    /*
     * PRUEBA 4: GET /api/v1/users/search/email/{mail} - Búsqueda por email
     * Endpoint crítico para autenticación y gestión de usuarios
     */
    @Test
    @DisplayName("GET /api/v1/users/search/email/{mail} → 200 con usuario encontrado")
    void findByMail_Ok() throws Exception {
        // Arrange
        UserResponse mockUser = createUserResponse(1, "admin1", "admin1@gym.com", "Juan Admin", "Administrador", "Gym Central");
        when(service.findByMail("admin1@gym.com")).thenReturn(mockUser);

        // Act & Assert
        mvc.perform(get(BASE + "/search/email/{mail}", "admin1@gym.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['User identifier']").value(1))
                .andExpect(jsonPath("$.user").value("admin1"))
                .andExpect(jsonPath("$.mail").value("admin1@gym.com"))
                .andExpect(jsonPath("$.name").value("Juan Admin"))
                .andExpect(jsonPath("$.role.role").value("Administrador"))
                .andExpect(jsonPath("$.gym.gym").value("Gym Central"));
    }

    @Test
    @DisplayName("GET /api/v1/users/search/email/{mail} no existente → 404")
    void findByMail_NotFound() throws Exception {
        // Arrange
        when(service.findByMail("nonexistent@gym.com")).thenThrow(new EntityNotFoundException("User not found"));

        // Act & Assert
        mvc.perform(get(BASE + "/search/email/{mail}", "nonexistent@gym.com"))
                .andExpect(status().isNotFound());
    }
}