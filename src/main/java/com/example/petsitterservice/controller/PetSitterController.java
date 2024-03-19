package com.example.petsitterservice.controller;

import com.example.petsitterservice.model.PetBoardingRequest;
import com.example.petsitterservice.model.dto.AvailabilityRequest;
import com.example.petsitterservice.model.PetSitter;
import com.example.petsitterservice.model.dto.PersonalRequestDto;
import com.example.petsitterservice.model.dto.PetSitterDashboard;
import com.example.petsitterservice.model.dto.SitterPageBoardingRequest;
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
@RequestMapping("/api/petSitters")
// @PreAuthorize("hasRole('SITTER')")
public class PetSitterController {

    private final PetServiceMainFacadeImpl petSitterService;
    private static final Logger logger = LoggerFactory.getLogger(PetSitterController.class);

    @Autowired
    public PetSitterController(PetServiceMainFacadeImpl petSitterService) {
        this.petSitterService = petSitterService;
    }

    @GetMapping("/dashboard/{sitterId}")
    public ResponseEntity<PetSitterDashboard> getSitterDashboard(@PathVariable Long sitterId) {
        PetSitter sitter = petSitterService.findSitterById(sitterId);
        PetSitterDashboard sitterDashboard = petSitterService.createSitterDashboard(sitter);
        return new ResponseEntity<>(sitterDashboard, HttpStatus.OK);
    }

    @PostMapping("/acceptRequest/{requestId}")
    public ResponseEntity<String> acceptRequest(@PathVariable Long requestId) {
        try {
            petSitterService.acceptRequest(requestId);
            return new ResponseEntity<>("Request accepted", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Некоторые даты из запроса не совпадают с доступными датами ситтера", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/declineRequest/{requestId}")
    public ResponseEntity<String> declineRequest(@PathVariable Long requestId) {
        petSitterService.declineRequest(requestId);
        return new ResponseEntity<>("Request declined", HttpStatus.OK);
    }

    @PostMapping("/toggleNewOrders/{userId}")
    public ResponseEntity<String> toggleNewOrders(@PathVariable Long userId,
                                             @RequestParam("newOrders") boolean newOrders) {
        petSitterService.updateTakingNewOrders(userId, newOrders);
        return new ResponseEntity<>("TakingNewOrders status updated", HttpStatus.OK);
    }

    @PostMapping("/changeAvailability/{userId}")
    public ResponseEntity<String> changeAvailableDates(@PathVariable Long userId,
                                                       @RequestBody AvailabilityRequest request) {

        String availableDates = request.getAvailableDates();
        PetSitter sitter = petSitterService.findSitterById(userId);
        petSitterService.addAvailabilityDates(sitter, availableDates);
        return new ResponseEntity<>("New dates added to availability list", HttpStatus.OK);
    }



}
