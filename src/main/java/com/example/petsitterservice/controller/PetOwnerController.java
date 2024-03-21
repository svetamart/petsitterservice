package com.example.petsitterservice.controller;

import com.example.petsitterservice.model.*;
import com.example.petsitterservice.model.dto.*;
import com.example.petsitterservice.service.PetServiceMainFacadeImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name="Владельцы питомцев", description="Управление запросами владельцев питомцев")

public class PetOwnerController {

    private final PetServiceMainFacadeImpl mainService;


    @Autowired
    public PetOwnerController(PetServiceMainFacadeImpl mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/{username}")
    @Operation(
            summary = "Поиск по имени",
            description = "Позволяет найти владельца питомца по юзернейму"
    )
    public ResponseEntity<PetOwner> getUserByUsername(
            @PathVariable @Parameter(description = "Имя пользователя") String username) {
        PetOwner user = mainService.getUserByUsername(username);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/dashboard/{userId}")
    @Operation(
            summary = "Личный кабинет",
            description = "Выдает личный кабинет владельца питомца"
    )
    public ResponseEntity<PetOwnerDashboard> getUserDashboard(
            @PathVariable @Parameter(description = "Идентификатор пользователя") Long userId) {
        PetOwner user = mainService.getUserById(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            PetOwnerDashboard ownerDashboard = mainService.createOwnerDashboard(user);
            return new ResponseEntity<>(ownerDashboard, HttpStatus.OK);
        }
    }

    @GetMapping("/dashboard/{userId}/myPets")
    @Operation(
            summary = "Список питомцев",
            description = "Выводит список питомцев пользователя"
    )
    public ResponseEntity<List<Pet>> getPets(
            @PathVariable @Parameter(description = "Идентификатор пользователя") Long userId) {
        PetOwner user = mainService.getUserById(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<Pet> pets = mainService.getPetsByOwner(user);
            return new ResponseEntity<>(pets, HttpStatus.OK);
        }
    }

    @PostMapping("/dashboard/{userId}/addPet")
    @Operation(
            summary = "Добавление питомца",
            description = "Добавляет питомца в список пользователя"
    )
    public ResponseEntity<String> addPet(
            @PathVariable @Parameter(description = "Идентификатор пользователя") Long userId,
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO, из которого строится объект питомца") PetDto pet) {
        Long id = pet.getUserId();
        PetOwner user = mainService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            mainService.addPet(pet, user);
            return ResponseEntity.ok("Pet successfully added.");
        }
    }


    @PostMapping("/dashboard/{userId}/createRequest")
    @Operation(
            summary = "Создание запроса на передержку",
            description = "Создает запрос на передержку домашнего животного"
    )
    public ResponseEntity<PetBoardingRequest> createPetBoardingRequest(
            @PathVariable @Parameter(description = "Идентификатор пользователя") Long userId,
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO, из которого строится запрос на передержку") PetBoardingRequestDto requestDto) {

        PetOwner user = mainService.getUserById(userId);
        Pet pet = mainService.getPetById(requestDto.getPetId());
        PetBoardingRequest request = mainService.createRequest(user, pet, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @GetMapping("/findSitters/{requestId}")
    @Operation(
            summary = "Поиск подходящих пет-ситтеров",
            description = "Находит пет-ситтеров, подходящих под требования запроса о передержке"
    )
    public ResponseEntity<List<SuitableSitterDto>> findSuitableSitters(
            @PathVariable @Parameter(description = "Идентификатор запроса на передержку") Long requestId) {

        PetBoardingRequest request = mainService.findRequestById(requestId);
        List<SuitableSitterDto> suitableSitters = mainService.findSuitableSitters(request);

        return new ResponseEntity<>(suitableSitters, HttpStatus.OK);
    }


    @PostMapping("/requests/{requestId}/chooseSitter/{sitterId}")
    @Operation(
            summary = "Выбор пет-ситтера",
            description = "Позволяет владельцу питомца выбрать понравившегося пет-ситтера и отправить ему запрос"
    )
    public ResponseEntity<String> chooseSitter(
            @PathVariable @Parameter(description = "Идентификатор запроса на передержку") Long requestId,
            @PathVariable @Parameter(description = "Идентификатор пет-ситтера") Long sitterId) {
        PetSitter sitter = mainService.findSitterById(sitterId);
        if (sitter != null) {
            mainService.assignSitter(requestId, sitter);
            return ResponseEntity.ok("Pet sitter is chosen.");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to choose pet sitter.");
    }

    @PostMapping("/{userId}/makePersonalRequest")
    @Operation(
            summary = "Персональная заявка",
            description = "Позволяет пользователю создать персональную заявку"
    )
    public ResponseEntity<String> makePersonalRequest (
            @PathVariable @Parameter(description = "Идентификатор пользователя") Long userId,
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO, из которого строится персональный запрос") PersonalRequestDto requestDto) {
        mainService.makePersonalRequest(requestDto);
        return new ResponseEntity<>("Personal request successfully created", HttpStatus.OK);
    }

    @PostMapping("/dashboard/addReview")
    @Operation(
            summary = "Добавление отзыва",
            description = "Позволяет пользователю оставить отзыв о пет-ситтере"
    )
    public ResponseEntity<String> addReview(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO, из которого строится отзыв") ReviewDto review) {
        mainService.createReview(review);
        return ResponseEntity.ok("Review successfully added.");
    }
}
