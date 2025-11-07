package com.example.proyecto.graphql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.proyecto.dto.RoleRequest;
import com.example.proyecto.dto.RoleResponse;
import com.example.proyecto.service.RoleService;

@Controller
public class RoleGraphql {
    @Autowired
    private RoleService roleService;

    @QueryMapping
    public List<RoleResponse> findAllRoles() {
        return roleService.findAll();
    }

    @QueryMapping
    public RoleResponse findByIdRole(@Argument Long id) {
        return roleService.findById(id);
    }

    @MutationMapping
    public RoleResponse createRole(@Argument RoleRequest roleRequest) {
        return roleService.create(roleRequest);
    }

    @MutationMapping
    public RoleResponse updateRole(@Argument Long id, @Argument RoleRequest roleRequest) {
        return roleService.update(id, roleRequest);
    }

    // @MutationMapping
    // public Boolean deleteRole(@Argument Long id) {
    //         roleService.delete(id);
    //         return true;
    // }

    @QueryMapping
    public List<RoleResponse> findByStatusRole(@Argument Integer statusValue) {
        List<RoleResponse> roles = roleService.findByStatus(statusValue);
        return roles;
    }
}