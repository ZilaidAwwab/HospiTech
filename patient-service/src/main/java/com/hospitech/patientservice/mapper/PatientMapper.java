package com.hospitech.patientservice.mapper;

import com.hospitech.patientservice.dto.PatientRequestDTO;
import com.hospitech.patientservice.dto.PatientResponseDTO;
import com.hospitech.patientservice.model.Patient;

import java.time.LocalDate;

public class PatientMapper {

    // The data coming from DB to FE will be converted to from Model class to DTO class
    // In that case the return type of method will be DTO, and argument will be Model
    // The data coming from FE to be stored in DB will be converted from DTO to Model class
    // In that case the return type of method will be Model class, and argument will be DTO

    // Here the data is coming from DB, shown on FE. So, return type is DTO, arg is Model
    public static PatientResponseDTO toDTO(Patient patient) {
        PatientResponseDTO patientDTO = new PatientResponseDTO();
        patientDTO.setId(patient.getId().toString());
        patientDTO.setName(patient.getName());
        patientDTO.setEmail(patient.getEmail());
        patientDTO.setAddress(patient.getAddress());
        patientDTO.setDateOfBirth(patient.getDateOfBirth().toString());
        return patientDTO;
    }

    // Here the data is coming from FE, stored in DB. So, return type is Model, arg is DTO
    public static Patient toModel(PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient();
        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()));
        return patient;
    }
}
