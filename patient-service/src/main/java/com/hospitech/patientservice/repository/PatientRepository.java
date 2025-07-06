package com.hospitech.patientservice.repository;

import com.hospitech.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    // If we want methods to expose to our services, and those are not available in this
    // Jpa Repo interface, then we mention those here, although these are mostly the methods
    // that JPA can expose, so we don't have to define then
    boolean existsByEmail(String email);

    // Since we are providing update functionality, so the email of the current should not be
    // a hindrance while updating his own data
    // This method checks, if the email of the user is same with a different ID
    boolean existsByEmailAndIdNot(String email, UUID id);
}
