package com.example.proyecto.graphql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.proyecto.dto.EmergencyContactRequest;
import com.example.proyecto.dto.EmergencyContactResponse;
import com.example.proyecto.service.EmergencyContactService;

@Controller
public class EmergencyContactGraphql {
    @Autowired
    private EmergencyContactService emergencyContactService;

    @QueryMapping
    public List<EmergencyContactResponse> findAllEmergencysContacts() {
        return emergencyContactService.findAll();
    }

    @QueryMapping
    public EmergencyContactResponse findByIdEmergencyContact(@Argument Long id) {
        return emergencyContactService.findById(id);
    }

    @MutationMapping
    public EmergencyContactResponse updateEmergencyContact(@Argument Long id, @Argument EmergencyContactRequest emergencyContactRequest) {
        return emergencyContactService.update(id, emergencyContactRequest);
    }

    // @MutationMapping
    // public Boolean deleteEmergencyContact(@Argument Long id) {
    //     emergencyContactService.delete(id);
    //     return true;
    // }

    @QueryMapping
    public List<EmergencyContactResponse> findAllEmergencysContactsPaginated(@Argument int page, @Argument int pageSize) {
        List<EmergencyContactResponse> emergencysContacts = emergencyContactService.getAll(page, pageSize);
        return emergencysContacts;
    }

    @QueryMapping
    public EmergencyContactResponse findByIdCustomerEmergencyContact(@Argument Long customerId) {
        return emergencyContactService.findEmergencyContactByIdCustomer(customerId);
    }
}