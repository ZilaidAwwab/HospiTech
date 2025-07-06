package com.hospitech.patientservice.controller;

import com.hospitech.patientservice.dto.PatientRequestDTO;
import com.hospitech.patientservice.dto.PatientResponseDTO;
import com.hospitech.patientservice.dto.validators.CreatePatientValidationGroup;
import com.hospitech.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patient", description = "API for managing patients") // For API Docs
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Get Patients")
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        List<PatientResponseDTO> patients = patientService.getPatients();
        return ResponseEntity.ok().body(patients);
    }

    // Since we had validation fields in the PatientRequestDTO object, that's why here the
    // @Valid annotation is added to ensure that validity of the data
    @PostMapping
    @Operation(summary = "Create/Add a new Patient")
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Validated({Default.class, CreatePatientValidationGroup.class})
            @RequestBody PatientRequestDTO patientRequestDTO) {

        PatientResponseDTO newPatient = patientService.createPatient(patientRequestDTO);
        return ResponseEntity.ok().body(newPatient);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Patient's Data")
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @PathVariable UUID id,
            @Validated({Default.class})
            @RequestBody PatientRequestDTO patientRequestDTO) {

        PatientResponseDTO updatedPatient = patientService.updatePatient(id, patientRequestDTO);
        return ResponseEntity.ok().body(updatedPatient);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Patient")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}

// @Validated({Default.class})
// It tells spring to validate the request using all the defaults that we specified in DTO

// In the create request the annotation is: @Validated({Default.class, CreatePatientValidationGroup.class})
// Here all the default fields are validated, along with the additional fields that contains "CreatePatientValidationGroup"

// Since the registered date field has CreatePatientValidationGroup property added, so this will be checked for create
// property (and that's why it is added in its controller request), and not in update so that won't be required for update
