package com.example.petsitterservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.example.petsitterservice.model.dto.AuthResponse;
import com.example.petsitterservice.model.dto.LoginRequest;
import com.example.petsitterservice.model.dto.OwnerRequest;
import com.example.petsitterservice.model.dto.SitterRequest;
import com.example.petsitterservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest login) {
        try {
            String username = login.getUsername();
            String password = login.getPassword();
            logger.info(username, password);
            AuthResponse authenticationResult = authService.login(username, password);

            return ResponseEntity.ok(authenticationResult);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body
                    (new AuthResponse("Authentication failed: " + e.getMessage(), null, null));
        }
    }

    @PostMapping("/registerOwner")
    public ResponseEntity<String> register(@RequestBody OwnerRequest user) {
        authService.registerPetOwner(user);
        return ResponseEntity.ok("Pet owner registered successfully");
    }

    @PostMapping("/registerSitter")
    public ResponseEntity<String> register(@RequestBody SitterRequest user) {
        authService.registerPetSitter(user);
        return ResponseEntity.ok("Pet sitter registered successfully");
    }

    @GetMapping("/logout/{userId}")
    public ResponseEntity<String> logout(@PathVariable Long userId) {
        authService.logout(userId);
        return ResponseEntity.ok("User logged out");
    }
}
