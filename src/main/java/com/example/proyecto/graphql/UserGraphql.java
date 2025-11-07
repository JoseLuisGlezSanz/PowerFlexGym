package com.example.proyecto.graphql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.proyecto.dto.UserRequest;
import com.example.proyecto.dto.UserResponse;
import com.example.proyecto.service.UserService;

@Controller
public class UserGraphql {
    @Autowired
    private UserService userService;

    @QueryMapping
    public List<UserResponse> findAllUsers() {
        return userService.findAll();
    }

    @QueryMapping
    public UserResponse findByIdUser(@Argument Long id) {
        return userService.findById(id);
    }

    @MutationMapping
    public UserResponse createUser(@Argument UserRequest userRequest) {
        return userService.create(userRequest);
    }

    @MutationMapping
    public UserResponse updateUser(@Argument Long id, @Argument UserRequest userRequest) {
        return userService.update(id, userRequest);
    }

    // @MutationMapping
    // public Boolean deleteUser(@Argument Long id) {
    //         userService.delete(id);
    //         return true;
    // }

    @QueryMapping
    public List<UserResponse> findAllUsersPaginated(@Argument int page,@Argument int pageSize) {
        return userService.getAll(page, pageSize);
    }

    @QueryMapping
    public UserResponse findByMailUser(@Argument String mail){
        return userService.findByMail(mail);
    }

    @QueryMapping
    public UserResponse findByNameUser(@Argument String nameUser) {
        return userService.findByUsername(nameUser);
    }

    @QueryMapping
    public List<UserResponse> findByRoleIdUser(@Argument Long roleId) {
        return userService.findByRoleId(roleId);
    }

    @QueryMapping
    public List<UserResponse> findByGymIdUser(@Argument Long gymId) {
        return userService.findByGymId(gymId);
    }
}