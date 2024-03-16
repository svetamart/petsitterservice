package com.example.petsitterservice.service;

import com.example.petsitterservice.model.*;
import com.example.petsitterservice.model.dto.PersonalRequestDto;
import com.example.petsitterservice.model.dto.PetBoardingRequestDto;

import java.util.List;

public interface PetServiceMainFacade {
    Pet getPetById(Long petId);
    void deletePetById(Long petId);
    List<Pet> getAllPets();
    List<Pet> getPetsByOwner(PetOwner owner);
    void addUser(PetOwner user);
    PetOwner getUserById(Long id);
    void deleteUser(Long id);
    List<PetOwner> getAllUsers();
    PetBoardingRequest createRequest(PetOwner user, Pet pet, PetBoardingRequestDto request);
    PetBoardingRequest findRequestById(Long requestId);

    List<PetSitter> getAllPetSitters();
    void addPetSitter(PetSitter sitter);
    void addAvailabilityDates(PetSitter petSitter, String availableDates);

    PetSitter findSitterById(Long id);
    List<PetSitter> findSuitableSitters(PetBoardingRequest request);

    List<PetBoardingRequest> getSitterRequests(Long sitterId);
    List<PetBoardingRequest> getUserRequests(Long userId);

    void updateRequestStatus(Long id, RequestStatus status);

    void acceptRequest(Long requestId);
    void declineRequest(Long requestId);

    void updateTakingNewOrders(Long id, boolean newOrders);
    void assignSitter (Long requestId, PetSitter sitter);

    void addPet(Pet pet, PetOwner owner);

    PetOwner getUserByUsername(String username);

    PetSitter getSitterByUsername(String username);

    void makePersonalRequest(PersonalRequestDto requestDto);
}
