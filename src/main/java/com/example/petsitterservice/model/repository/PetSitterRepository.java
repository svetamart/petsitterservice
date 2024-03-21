package com.example.petsitterservice.model.repository;

import com.example.petsitterservice.model.PetSitter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetSitterRepository extends JpaRepository<PetSitter, Long> {
    PetSitter findByUsername(String username);
}
