package com.example.petsitterservice.model.repository;

import com.example.petsitterservice.model.Pet;
import com.example.petsitterservice.model.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Репозиторий с животными
 */
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwner(PetOwner owner);
}
