package com.example.petsitterservice.service;

import com.example.petsitterservice.model.*;
import com.example.petsitterservice.model.dto.PersonalRequestDto;
import com.example.petsitterservice.model.dto.PetBoardingRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PetServiceMainFacadeImpl implements PetServiceMainFacade{

    private final PetService petService;
    private final PetOwnerService userService;
    private final PetBoardingRequestService boardingRequestService;
    private final PetSitterService petSitterService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final PersonalRequestService personalRequestService;

    @Autowired
    public PetServiceMainFacadeImpl(PetService petService, PetOwnerService userService,
                                    PetBoardingRequestService boardingRequestService, PetSitterService petSitterService,
                                    BCryptPasswordEncoder passwordEncoder,
                                    PersonalRequestService personalRequestService) {
        this.petService = petService;
        this.userService = userService;
        this.boardingRequestService = boardingRequestService;
        this.petSitterService = petSitterService;
        this.passwordEncoder = passwordEncoder;
        this.personalRequestService = personalRequestService;
    }
    @Override
    public Pet getPetById(Long petId) {
        return petService.getPetById(petId);
    }

    @Override
    public void deletePetById(Long petId) {
        petService.deletePetById(petId);
    }

    @Override
    public List<Pet> getAllPets() {
        return petService.getAllPets();
    }

    @Override
    public List<Pet> getPetsByOwner(PetOwner owner) {
        return petService.getPetsByOwner(owner);
    }

    @Override
    public void addUser(PetOwner user) {
        userService.addUser(user);
    }

    @Override
    public PetOwner getUserById(Long id) {
        return userService.getUserById(id);
    }

    @Override
    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }

    @Override
    public List<PetOwner> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public PetBoardingRequest createRequest(PetOwner user, Pet pet, PetBoardingRequestDto request) {
        return boardingRequestService.createRequest(user, pet, request);
    }

    @Override
    public PetBoardingRequest findRequestById(Long requestId) {
        return boardingRequestService.findById(requestId);
    }

    @Override
    public void addPetSitter(PetSitter sitter) {
        petSitterService.addSitter(sitter);
    }

    @Override
    public List<PetSitter> getAllPetSitters() {
        return petSitterService.getAllPetSitters();
    }



    @Override
    public void addAvailabilityDates(PetSitter petSitter, String availableDates) {
        petSitterService.addAvailabilityDates(petSitter, availableDates);
    }

    @Override
    public List<PetSitter> findSuitableSitters(PetBoardingRequest request) {
        return petSitterService.findSuitableSitters(request);
    }

    @Override
    public PetSitter findSitterById(Long id) {
        return petSitterService.getById(id);
    }

    @Override
    public List<PetBoardingRequest> getSitterRequests(Long sitterId) {
        PetSitter sitter = petSitterService.getById(sitterId);
        if (sitter != null) {
            return sitter.getRequests();
        } else {
            return null;
        }
    }

    @Override
    public List<PetBoardingRequest> getUserRequests(Long userId) {
        PetOwner user = userService.getUserById(userId);
        if (user != null) {
            return user.getRequests();
        } else {
            return null;
        }
    }

    @Override
    public void updateRequestStatus(Long id, RequestStatus status) {
        boardingRequestService.updateRequestStatus(id, status);
    }

    @Override
    public void acceptRequest(Long requestId) {
        petSitterService.acceptRequest(requestId);
    }

    @Override
    public void declineRequest(Long requestId) {
        petSitterService.declineRequest(requestId);
    }

    @Override
    public void updateTakingNewOrders(Long id, boolean newOrders) {
        petSitterService.updateTakingNewOrders(id, newOrders);
    }

    @Override
    public void assignSitter (Long requestId, PetSitter sitter) {
        boardingRequestService.assignSitter(requestId, sitter);
    }

    @Override
    public void addPet(Pet pet, PetOwner owner) {
        petService.addPet(pet, owner);
    }

    public static String formatAge(int age, String ageUnit) {
        if ("years".equals(ageUnit)) {
            return formatYears(age);
        } else if ("months".equals(ageUnit)) {
            return formatMonths(age);
        } else {
            return age + " " + ageUnit;
        }
    }

    private static String formatYears(int years) {
        if (years < 0) {
            return "Некорректный возраст";
        }

        if (years >= 11 && years <= 19) {
            return years + " лет";
        }

        int lastDigit = years % 10;
        if (lastDigit == 1) {
            return years + " год";
        } else if (lastDigit >= 2 && lastDigit <= 4) {
            return years + " года";
        } else {
            return years + " лет";
        }
    }

    private static String formatMonths(int months) {
        if (months < 0 || months >= 12) {
            return "Некорректный возраст";
        }

        if (months >= 2 && months <= 4) {
            return months + " месяца";
        }
        return months + " месяцев";
    }

    @Override
    public PetOwner getUserByUsername(String username) {
        return userService.getUserByName(username);
    }

    @Override
    public PetSitter getSitterByUsername(String username) {
        return petSitterService.getUserByName(username);
    }

    @Override
    public void makePersonalRequest(PersonalRequestDto requestDto) {
        PetOwner owner = userService.getUserById(requestDto.getUserId());
        PetBoardingRequest boardingRequest = boardingRequestService.findById(requestDto.getRequestId());
        PersonalRequest request = new PersonalRequest();
        request.setOwner(owner);
        request.setRequest(boardingRequest);
        request.setPhone(requestDto.getPhone());
        personalRequestService.addRequest(request);
        boardingRequestService.updateRequestStatus(boardingRequest.getId(), RequestStatus.PERSONAL);
    }

    public boolean isPasswordCorrect(String enteredPassword, String actualPassword) {
        return passwordEncoder.matches(enteredPassword, actualPassword);
    }

    public void registerPetOwner(PetOwner user) {
        userService.register(user);
    }

    public void registerPetSitter(PetSitter user) {
        petSitterService.register(user);
    }
}
