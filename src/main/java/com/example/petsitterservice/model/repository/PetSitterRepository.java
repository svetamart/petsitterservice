package com.example.petsitterservice.model.repository;

import com.example.petsitterservice.model.PetSitter;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий с пет-ситтерами
 */

public interface PetSitterRepository extends JpaRepository<PetSitter, Long> {
    PetSitter findByUsername(String username);
}
