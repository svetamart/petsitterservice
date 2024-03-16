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





}
