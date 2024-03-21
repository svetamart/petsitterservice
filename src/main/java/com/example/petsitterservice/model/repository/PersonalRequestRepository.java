package com.example.petsitterservice.model.repository;

import com.example.petsitterservice.model.PersonalRequest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий с персональными заявками
 */

public interface PersonalRequestRepository extends JpaRepository<PersonalRequest, Long> {
}
