package com.example.petsitterservice.service;

import com.example.petsitterservice.model.*;
import com.example.petsitterservice.model.dto.*;

import java.util.List;

/**
 * Интерфейс для фасада основных операций приложения, связанных с управлением питомцами,
 * пользователями, запросами на передержку и отзывами.
 */
public interface PetServiceMainFacade {

    /**
     * Методы для управления питомцами.
     */
    Pet getPetById(Long petId);
    void deletePetById(Long petId);
    List<Pet> getAllPets();
    List<Pet> getPetsByOwner(PetOwner owner);
    void addPet(PetDto pet, PetOwner owner);

    /**
     * Методы для управления владельцами питомцев.
     */
    void registerPetOwner(PetOwner user);
    PetOwner getUserByUsername(String username);
    void addUser(PetOwner user);
    PetOwner getUserById(Long id);
    void deleteUser(Long id);
    List<PetOwner> getAllUsers();
    List<PetBoardingRequest> getUserRequests(Long userId);
    void makePersonalRequest(PersonalRequestDto requestDto);
    PetOwnerDashboard createOwnerDashboard(PetOwner user);


    /**
     * Методы для управления запросами на передержку.
     */
    PetBoardingRequest findRequestById(Long requestId);
    void updateRequestStatus(Long id, RequestStatus status);
    void assignSitter (Long requestId, PetSitter sitter);
    PetBoardingRequest createRequest(PetOwner user, Pet pet, PetBoardingRequestDto request);


    /**
     * Методы для управления пет-ситтерами.
     */
    void registerPetSitter(PetSitter user);
    List<PetSitter> getAllPetSitters();
    void addPetSitter(PetSitter sitter);
    void addAvailabilityDates(PetSitter petSitter, String availableDates);
    PetSitter findSitterById(Long id);
    List<SuitableSitterDto> findSuitableSitters(PetBoardingRequest request);
    List<PetBoardingRequest> getSitterRequests(Long sitterId);
    void acceptRequest(Long requestId);
    void declineRequest(Long requestId);
    void updateTakingNewOrders(Long id, boolean newOrders);
    PetSitter getSitterByUsername(String username);
    PetSitterDashboard createSitterDashboard(PetSitter sitter);

    /**
     * Методы для удаления пользователей и активации/деактивации аккаунтов.
     */
    void deleteUserByRole(String userRole, Long userId);
    void activateAccountByRole(String userRole, Long userId);
    void deactivateAccountByRole(String userRole, Long userId);

    /**
     * Методы для управления отзывами.
     */
    void createReview(ReviewDto review);
    void deleteReview(Long id);
}
