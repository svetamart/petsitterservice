package com.example.petsitterservice.model.repository;

import com.example.petsitterservice.model.Pet;
import com.example.petsitterservice.model.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwner(PetOwner owner);
}
