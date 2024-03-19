package com.example.petsitterservice.controller;


import com.example.petsitterservice.model.PetOwner;
import com.example.petsitterservice.model.PetSitter;
import com.example.petsitterservice.service.PetServiceMainFacadeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/main")
public class MainController {

    private final PetServiceMainFacadeImpl mainService;

    @Autowired
    public MainController (PetServiceMainFacadeImpl mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/showUsers")
    public ResponseEntity<List<PetOwner>> showAllUsers() {
        List<PetOwner> users = mainService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/showSitters")
    public ResponseEntity<List<PetSitter>> showAllPetSitters() {
        List<PetSitter> sitters = mainService.getAllPetSitters();
        return new ResponseEntity<>(sitters, HttpStatus.OK);

    }

    // ДОБАВИТЬ методы удаления пользователя + активирование и деактивирование аккаунтов

    @DeleteMapping("/deleteUser/{userRole}/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userRole, @PathVariable Long userId) {
        try {
            mainService.deleteUserByRole(userRole, userId);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while deleting the user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/activateAccount/{userRole}/{userId}")
    public ResponseEntity<String> activateAccount(@PathVariable String userRole, @PathVariable Long userId) {
        try {
            mainService.activateAccountByRole(userRole, userId);
            return new ResponseEntity<>("Account activated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while activating the account", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/deactivateAccount/{userRole}/{userId}")
    public ResponseEntity<String> deactivateAccount(@PathVariable String userRole, @PathVariable Long userId) {
        try {
            mainService.deactivateAccountByRole(userRole, userId);
            return new ResponseEntity<>("Account deactivated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while deactivating the account", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteReview/{reviewId}")
    public ResponseEntity<String> deleteReview (@PathVariable Long id) {
        mainService.deleteReview(id);
        return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
    }
}
