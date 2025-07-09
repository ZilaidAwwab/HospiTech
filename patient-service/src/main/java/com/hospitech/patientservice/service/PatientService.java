package com.hospitech.patientservice.service;

import com.hospitech.patientservice.exception.EmailAlreadyExistsException;
import com.hospitech.patientservice.dto.PatientRequestDTO;
import com.hospitech.patientservice.dto.PatientResponseDTO;
import com.hospitech.patientservice.exception.PatientNotFoundException;
import com.hospitech.patientservice.grpc.BillingServicesGrpcClient;
import com.hospitech.patientservice.mapper.PatientMapper;
import com.hospitech.patientservice.model.Patient;
import com.hospitech.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    // Injecting dependency for gRPC client, utilizing the Billing services via gRPC
    private final BillingServicesGrpcClient billingServicesGrpcClient;

    public PatientService(
            PatientRepository patientRepository,
            BillingServicesGrpcClient billingServicesGrpcClient
    ) {
        this.patientRepository = patientRepository;
        this.billingServicesGrpcClient = billingServicesGrpcClient;
    }

    // *** Get all patients data ***
    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientResponseDTO> patientResponseDTOS = patients.stream().map(
                PatientMapper::toDTO).toList();
        return patientResponseDTOS;

        // These Both are equivalent
        // map(patient -> PatientMapper.toDTO(patient)) (Lambda)
        // map(PatientMapper::toDTO)                    (Method Reference)
    }

    // *** Create a patient ***
    // Other than CRUDs we can implement business logic in services as well
    // As checking if the email already exists or so
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {

        // Checking if the email already exists (this is the custom method created in JPA)
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "A patient with this email already exists " + patientRequestDTO.getEmail());
        }

        Patient newPatient = patientRepository.save(
                PatientMapper.toModel(patientRequestDTO));

        // utilizing the billing service via gRPC client
        billingServicesGrpcClient.createBillingAccount(
                newPatient.getId().toString(), newPatient.getName(), newPatient.getEmail());

        return PatientMapper.toDTO(newPatient);
    }

    // *** Update patient's data ***
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {

        // Fetching Patient's Data
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with ID: " + id));

        // Email Existence Check, although we want to exclude the email of the user selected
        // otherwise we won't be able to edit other info of the user, except email
        // For this purpose we created another method in the repository class
        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException(
                    "A patient with this email already exists " + patientRequestDTO.getEmail());
        }

        // Updating the Data in the DB
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        // Saving the Data
        Patient updatedPatient = patientRepository.save(patient);

        // returning updated data back to client
        return PatientMapper.toDTO(updatedPatient);
    }

    // *** Delete Patient's Data ***
    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
