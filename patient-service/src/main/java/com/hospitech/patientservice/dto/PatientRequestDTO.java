package com.hospitech.patientservice.dto;

import com.hospitech.patientservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRequestDTO {

    // adding annotations for data validation (coming from client side)
    @NotBlank
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email Should be valid")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Date of birth is required")
    private String dateOfBirth;

    // Since this is a field that is required for creation, and not for update, that's why groups filed is added here
    @NotBlank(groups = CreatePatientValidationGroup.class,
            message = "Registered date is required")
    private String registeredDate;
}
