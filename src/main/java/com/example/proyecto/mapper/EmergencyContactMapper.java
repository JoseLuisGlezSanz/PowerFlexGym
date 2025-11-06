package com.example.proyecto.mapper;

import com.example.proyecto.dto.EmergencyContactRequest;
import com.example.proyecto.dto.EmergencyContactResponse;
import com.example.proyecto.model.EmergencyContact;

public class EmergencyContactMapper {
    public static EmergencyContactResponse toResponse(EmergencyContact emergencyContact) {
        if (emergencyContact == null) return null;
        
        return EmergencyContactResponse.builder()
                .idContact(emergencyContact.getIdContact())
                .contactName(emergencyContact.getContactName())
                .contactPhone(emergencyContact.getContactPhone())
                .build();
    }

    public static EmergencyContact toEntity(String contactName, String contactPhone) {
        if (contactName == null && contactPhone == null) return null;
        
        return EmergencyContact.builder()
                .contactName(contactName)
                .contactPhone(contactPhone)
                .build();
    }

    public static void copyToEntity(EmergencyContactRequest dto, EmergencyContact entity) {
        if (dto == null || entity == null) return;
        
        entity.setContactName(dto.getContactName());
        entity.setContactPhone(dto.getContactPhone());
    }
}