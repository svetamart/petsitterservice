package com.example.petsitterservice.controller;

import com.example.petsitterservice.model.*;
import com.example.petsitterservice.model.dto.*;
import com.example.petsitterservice.service.PetServiceMainFacadeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class PetOwnerController {

    private final PetServiceMainFacadeImpl mainService;
    private static final Logger logger = LoggerFactory.getLogger(PetOwnerController.class);


    @Autowired
    public PetOwnerController(PetServiceMainFacadeImpl mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/{username}")
    // @PreAuthorize("#userId == principal.id")
    public ResponseEntity<PetOwner> getUserByUsername(@PathVariable String username) {
        PetOwner user = mainService.getUserByUsername(username);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            logger.info("user == null");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/dashboard/{userId}")
    public ResponseEntity<PetOwnerDashboard> getUserDashboard(@PathVariable Long userId) {
        PetOwner user = mainService.getUserById(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            logger.info(user.toString());
            PetOwnerDashboard ownerDashboard = new PetOwnerDashboard();
            ownerDashboard.setId(user.getId());
            ownerDashboard.setUsername(user.getUsername());
            ownerDashboard.setName(user.getName());
            ownerDashboard.setLastName(user.getLastName());
            ownerDashboard.setCity(user.getCity());
            ownerDashboard.setPets(user.getPets());


            List<PetBoardingRequest> requests = user.getRequests();
            List<OwnerPageBoardingRequest> sitterPageRequests = new ArrayList<>();
            for (PetBoardingRequest boardingRequest : requests) {
                OwnerPageBoardingRequest ownerPageRequest = OwnerPageBoardingRequest.fromPetBoardingRequest(boardingRequest);
                sitterPageRequests.add(ownerPageRequest);
            }
            ownerDashboard.setRequests(sitterPageRequests);
            return new ResponseEntity<>(ownerDashboard, HttpStatus.OK);
        }
    }

    @GetMapping("/dashboard/{userId}/myPets")
    public ResponseEntity<List<Pet>> getPets(@PathVariable Long userId) {
        PetOwner user = mainService.getUserById(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<Pet> pets = mainService.getPetsByOwner(user);
            return new ResponseEntity<>(pets, HttpStatus.OK);
        }
    }

    @PostMapping("/dashboard/{userId}/addPet")
    public ResponseEntity<String> addPet(@PathVariable Long userId, @RequestBody PetDto pet) {
        Long id = pet.getUserId();
        PetOwner user = mainService.getUserById(id);
        if (user == null) {
            logger.info("user not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            logger.info("adding pet");
            logger.info(pet.toString());
            Pet newPet = new Pet();
            newPet.setName(pet.getName());

            String ageString = PetServiceMainFacadeImpl.formatAge(pet.getAge(), pet.getAgeUnit());
            newPet.setAge(ageString);

            newPet.setSize(pet.getSize());
            newPet.setSpecies(pet.getSpecies());
            newPet.setBreed(pet.getBreed());
            newPet.setGender(pet.getGender());
            newPet.setSterilized(pet.isSterilized());

            mainService.addPet(newPet, user);
            return ResponseEntity.ok("Pet successfully added.");
        }
    }


    @PostMapping("/dashboard/{userId}/createRequest")
    public ResponseEntity<PetBoardingRequest> createPetBoardingRequest(@PathVariable Long userId,
                                                                              @RequestBody PetBoardingRequestDto requestDto) {

        PetOwner user = mainService.getUserById(userId);
        Pet pet = mainService.getPetById(requestDto.getPetId());
        PetBoardingRequest request = mainService.createRequest(user, pet, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @GetMapping("/findSitters/{requestId}")
    public ResponseEntity<List<PetSitter>> findSuitableSitters(
            @PathVariable Long requestId) {

        PetBoardingRequest request = mainService.findRequestById(requestId);
        List<PetSitter> suitableSitters = mainService.findSuitableSitters(request);

        return new ResponseEntity<>(suitableSitters, HttpStatus.OK);
    }


    @PostMapping("/requests/{requestId}/chooseSitter/{sitterId}")
    public ResponseEntity<String> chooseSitter(@PathVariable Long requestId, @PathVariable Long sitterId) {
        PetSitter sitter = mainService.findSitterById(sitterId);
        if (sitter != null) {
            mainService.assignSitter(requestId, sitter);
            return ResponseEntity.ok("Pet sitter is chosen.");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to choose pet sitter.");
    }

    @PostMapping("/{userId}/makePersonalRequest")
    public ResponseEntity<String> makePersonalRequest (@PathVariable Long userId, @RequestBody PersonalRequestDto requestDto) {
        logger.info("Creating personal request API");
        mainService.makePersonalRequest(requestDto);
        return new ResponseEntity<>("Personal request successfully created", HttpStatus.OK);
    }
}
