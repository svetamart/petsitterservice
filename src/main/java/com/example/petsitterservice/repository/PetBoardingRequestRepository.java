package com.example.petsitterservice.repository;

import com.example.petsitterservice.model.PetBoardingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetBoardingRequestRepository extends JpaRepository<PetBoardingRequest, Long> {
}
