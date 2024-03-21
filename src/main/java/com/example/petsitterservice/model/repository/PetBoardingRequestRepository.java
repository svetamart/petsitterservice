package com.example.petsitterservice.model.repository;

import com.example.petsitterservice.model.PetBoardingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий с запросами на передержку
 */

public interface PetBoardingRequestRepository extends JpaRepository<PetBoardingRequest, Long> {
}
