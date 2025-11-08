package com.example.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.proyecto.dto.UserRequest;
import com.example.proyecto.dto.UserResponse;
import com.example.proyecto.service.UserService;
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

    /*
     * ===========================
     * Config de test: mock beans
     * ===========================
     */
    @TestConfiguration
    static class TestConfig {
        @Bean
        UserService userService() {
            return mock(UserService.class);
        }
    }

    /*
     * ===========================
     * Helpers DTO
     * ===========================
     */
    private UserResponse resp(Long id, String nameUser, String mail, String phone, String name, 
                             String password, Integer status, Long roleId, Long gymId) {
        return UserResponse.builder()
                .id(id)
                .nameUser(nameUser)
                .mail(mail)
                .phone(phone)
                .name(name)
                .password(password)
                .status(status)
                .roleId(roleId)
                .gymId(gymId)
                .build();
    }

    private UserResponse resp(Long id, String nameUser, String mail, String name) {
        return resp(id, nameUser, mail, "1234567890", name, "encryptedPassword", 1, 1L, 1L);
    }

    private UserRequest req(String nameUser, String mail, String phone, String name, 
                           String password, Integer status, Long roleId, Long gymId) {
        UserRequest r = new UserRequest();
        r.setNameUser(nameUser);
        r.setMail(mail);
        r.setPhone(phone);
        r.setName(name);
        r.setPassword(password);
        r.setStatus(status);
        r.setRoleId(roleId);
        r.setGymId(gymId);
        return r;
    }

    private UserRequest req(String nameUser, String mail, String name, String password) {
        return req(nameUser, mail, "1234567890", name, password, 1, 1L, 1L);
    }

    /*
     * ================================
     * GET /api/v1/users
     * ================================
     */

    @Test
    @DisplayName("GET /api/v1/users → 200 con lista")
    void findAll_Ok() throws Exception {
        when(service.findAll()).thenReturn(List.of(
            resp(1L, "juan_perez", "juan@email.com", "Juan Pérez"),
            resp(2L, "maria_garcia", "maria@email.com", "María García"),
            resp(3L, "admin_user", "admin@email.com", "Admin User")
        ));

        mvc.perform(get(BASE).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].['User identifier']").value(1))
                .andExpect(jsonPath("$[0].nameUser").value("juan_perez"))
                .andExpect(jsonPath("$[0].mail").value("juan@email.com"))
                .andExpect(jsonPath("$[0].name").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].status").value(1))
                .andExpect(jsonPath("$[1].['User identifier']").value(2))
                .andExpect(jsonPath("$[1].nameUser").value("maria_garcia"));
    }

    @Test
    @DisplayName("GET /api/v1/users → 200 con lista vacía")
    void findAll_empty() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * ====================================
     * GET /api/v1/users/mail/{mail}
     * ====================================
     */

    @Test
    @DisplayName("GET /mail/{mail} existente → 200")
    void findByMail_ok() throws Exception {
        when(service.findByMail("juan@email.com")).thenReturn(
            resp(1L, "juan_perez", "juan@email.com", "Juan Pérez")
        );

        mvc.perform(get(BASE + "/mail/{mail}", "juan@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['User identifier']").value(1))
                .andExpect(jsonPath("$.nameUser").value("juan_perez"))
                .andExpect(jsonPath("$.mail").value("juan@email.com"))
                .andExpect(jsonPath("$.name").value("Juan Pérez"));
    }

    @Test
    @DisplayName("GET /mail/{mail} no existente → 404")
    void findByMail_notFound() throws Exception {
        when(service.findByMail("nonexistent@email.com")).thenThrow(new EntityNotFoundException("User not found"));

        mvc.perform(get(BASE + "/mail/{mail}", "nonexistent@email.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    /*
     * ==========================================
     * GET /api/v1/users/userName/{nameUser}
     * ==========================================
     */

    @Test
    @DisplayName("GET /userName/{nameUser} existente → 200")
    void findByUsername_ok() throws Exception {
        when(service.findByUsername("admin_user")).thenReturn(
            resp(3L, "admin_user", "admin@email.com", "Admin User")
        );

        mvc.perform(get(BASE + "/userName/{nameUser}", "admin_user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['User identifier']").value(3))
                .andExpect(jsonPath("$.nameUser").value("admin_user"))
                .andExpect(jsonPath("$.mail").value("admin@email.com"))
                .andExpect(jsonPath("$.name").value("Admin User"));
    }

    @Test
    @DisplayName("GET /userName/{nameUser} no existente → 404")
    void findByUsername_notFound() throws Exception {
        when(service.findByUsername("unknown_user")).thenThrow(new EntityNotFoundException("User not found"));

        mvc.perform(get(BASE + "/userName/{nameUser}", "unknown_user"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    /*
     * ====================================
     * GET /api/v1/users/role/{roleId}
     * ====================================
     */

    @Test
    @DisplayName("GET /role/{roleId} sin resultados → 200 con lista vacía")
    void findByRoleId_empty() throws Exception {
        when(service.findByRoleId(999L)).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/role/{roleId}", 999L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * ====================================
     * GET /api/v1/users/gym/{gymId}
     * ====================================
     */
    
    @Test
    @DisplayName("GET /gym/{gymId} sin resultados → 200 con lista vacía")
    void findByGymId_empty() throws Exception {
        when(service.findByGymId(999L)).thenReturn(Collections.emptyList());

        mvc.perform(get(BASE + "/gym/{gymId}", 999L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
     * =================================
     * GET /api/v1/users/{id}
     * =================================
     */

    @Test
    @DisplayName("GET /{id} existente → 200")
    void findById_ok() throws Exception {
        when(service.findById(5L)).thenReturn(
            resp(5L, "special_user", "special@email.com", "5555555555", "Special User", 
                 "encryptedPass", 1, 2L, 3L)
        );

        mvc.perform(get(BASE + "/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['User identifier']").value(5))
                .andExpect(jsonPath("$.nameUser").value("special_user"))
                .andExpect(jsonPath("$.mail").value("special@email.com"))
                .andExpect(jsonPath("$.phone").value("5555555555"))
                .andExpect(jsonPath("$.name").value("Special User"))
                .andExpect(jsonPath("$.password").value("encryptedPass"))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.roleId").value(2))
                .andExpect(jsonPath("$.gymId").value(3));
    }

    @Test
    @DisplayName("GET /{id} no existente → 404")
    void findById_notFound() throws Exception {
        when(service.findById(999L)).thenThrow(new EntityNotFoundException("User not found"));

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
     * POST /api/v1/users
     * ================================
     */

    @Test
    @DisplayName("POST create válido → 201 + Location + body")
    void create_ok() throws Exception {
        UserRequest rq = req("new_user", "new@email.com", "New User", "plainPassword");
        UserResponse created = resp(1234L, "new_user", "new@email.com", "New User");
        when(service.create(any(UserRequest.class))).thenReturn(created);

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/users/1234"))
                .andExpect(jsonPath("$.['User identifier']").value(1234))
                .andExpect(jsonPath("$.nameUser").value("new_user"))
                .andExpect(jsonPath("$.mail").value("new@email.com"))
                .andExpect(jsonPath("$.name").value("New User"))
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
        UserRequest rq = req("", "", "", "");

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isBadRequest());
    }

    /*
     * ================================
     * PUT /api/v1/users/{id}
     * ================================
     */

    @Test
    @DisplayName("PUT update válido → 200 con body actualizado")
    void update_ok() throws Exception {
        UserRequest rq = req("updated_user", "updated@email.com", "6666666666", "Updated User", 
                           "newPassword", 0, 3L, 4L);
        UserResponse updated = resp(10L, "updated_user", "updated@email.com", "6666666666", 
                                  "Updated User", "encryptedNewPass", 0, 3L, 4L);
        when(service.update(eq(10L), any(UserRequest.class))).thenReturn(updated);

        mvc.perform(put(BASE + "/{id}", 10L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['User identifier']").value(10))
                .andExpect(jsonPath("$.nameUser").value("updated_user"))
                .andExpect(jsonPath("$.mail").value("updated@email.com"))
                .andExpect(jsonPath("$.phone").value("6666666666"))
                .andExpect(jsonPath("$.name").value("Updated User"))
                .andExpect(jsonPath("$.status").value(0))
                .andExpect(jsonPath("$.roleId").value(3))
                .andExpect(jsonPath("$.gymId").value(4));
    }

    @Test
    @DisplayName("PUT update en no existente → 404")
    void update_notFound() throws Exception {
        when(service.update(eq(9999L), any(UserRequest.class)))
                .thenThrow(new EntityNotFoundException("User not found"));

        mvc.perform(put(BASE + "/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req("test", "test@email.com", "Test User", "pass"))))
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
     * GET /api/v1/users/paginationAll
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
            resp(100L, "paged_user1", "paged1@email.com", "Paged User One"),
            resp(101L, "paged_user2", "paged2@email.com", "Paged User Two")
        ));

        mvc.perform(get(BASE + "/paginationAll")
                .queryParam("page", String.valueOf(page))
                .queryParam("pageSize", String.valueOf(size))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].['User identifier']").value(100))
                .andExpect(jsonPath("$[0].nameUser").value("paged_user1"))
                .andExpect(jsonPath("$[1].['User identifier']").value(101));
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
        when(service.getAll(0, 10)).thenReturn(List.of(resp(1L, "default_user", "default@email.com", "Default User")));

        mvc.perform(get(BASE + "/paginationAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nameUser").value("default_user"));
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
        when(service.findAll()).thenReturn(List.of(resp(1L, "test_user", "test@email.com", "Test User")));

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
    @DisplayName("POST create con email duplicado → 400 (depende de validación)")
    void create_duplicateEmail() throws Exception {
        UserRequest rq = req("new_user", "existing@email.com", "New User", "password");

        mvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST create con username con espacios → 400")
    void create_usernameWithSpaces() throws Exception {
        UserRequest rq = req("user name", "user@email.com", "User Name", "password");

        mvc.perform(post(BASE)
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
}