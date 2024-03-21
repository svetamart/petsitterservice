package com.example.petsitterservice.controller;

import com.example.petsitterservice.model.dto.AvailabilityRequest;
import com.example.petsitterservice.model.PetSitter;
import com.example.petsitterservice.model.dto.PetSitterDashboard;
import com.example.petsitterservice.service.PetServiceMainFacadeImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/petSitters")
@Tag(name="Пет-ситтеры", description="Управление запросами пет-ситтеров")
public class PetSitterController {

    private final PetServiceMainFacadeImpl petSitterService;

    @Autowired
    public PetSitterController(PetServiceMainFacadeImpl petSitterService) {
        this.petSitterService = petSitterService;
    }

    @GetMapping("/dashboard/{sitterId}")
    @Operation(
            summary = "Личный кабинет",
            description = "Выдает личный кабинет пет-ситтера"
    )
    public ResponseEntity<PetSitterDashboard> getSitterDashboard(@PathVariable Long sitterId) {
        PetSitter sitter = petSitterService.findSitterById(sitterId);
        PetSitterDashboard sitterDashboard = petSitterService.createSitterDashboard(sitter);
        return new ResponseEntity<>(sitterDashboard, HttpStatus.OK);
    }

    @PostMapping("/acceptRequest/{requestId}")
    @Operation(
            summary = "Принять запрос",
            description = "Позволяет пет-ситтеру принять запрос на передержку"
    )
    public ResponseEntity<String> acceptRequest(@PathVariable Long requestId) {
        try {
            petSitterService.acceptRequest(requestId);
            return new ResponseEntity<>("Request accepted", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Некоторые даты из запроса не совпадают с доступными датами ситтера", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/declineRequest/{requestId}")
    @Operation(
            summary = "Отклонить запрос",
            description = "Позволяет пет-ситтеру отклонить запрос на передержку"
    )
    public ResponseEntity<String> declineRequest(@PathVariable Long requestId) {
        petSitterService.declineRequest(requestId);
        return new ResponseEntity<>("Request declined", HttpStatus.OK);
    }

    @PostMapping("/toggleNewOrders/{userId}")
    @Operation(
            summary = "Статус поступления новых запросов",
            description = "Позволяет пет-ситтеру возобносить или приостановить поступление новых запросов на передержку"
    )
    public ResponseEntity<String> toggleNewOrders(@PathVariable Long userId,
                                             @RequestParam("newOrders") boolean newOrders) {
        petSitterService.updateTakingNewOrders(userId, newOrders);
        return new ResponseEntity<>("TakingNewOrders status updated", HttpStatus.OK);
    }

    @PostMapping("/changeAvailability/{userId}")
    @Operation(
            summary = "Доступные для передержки даты",
            description = "Позволяет пет-ситтеру настроить даты, в которое он готов брать животных на передержку"
    )
    public ResponseEntity<String> changeAvailableDates(@PathVariable Long userId,
                                                       @RequestBody AvailabilityRequest request) {

        String availableDates = request.getAvailableDates();
        PetSitter sitter = petSitterService.findSitterById(userId);
        petSitterService.addAvailabilityDates(sitter, availableDates);
        return new ResponseEntity<>("New dates added to availability list", HttpStatus.OK);
    }



}
