package com.example.petsitterservice.model.repository;

import com.example.petsitterservice.model.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetOwnerRepository extends JpaRepository<PetOwner, Long> {

    PetOwner findByUsername(String username);

}
