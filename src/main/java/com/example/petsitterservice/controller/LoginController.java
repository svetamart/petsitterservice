package com.example.petsitterservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name="Авторизация и аутентификация", description="Обработка запросов, связанных с аутентификацией и авторизацией пользователей в системе")
public class LoginController {

    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Вход в систему",
            description = "Позволяет залогинить пользователя"
    )
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
    @Operation(
            summary = "Регистрация владельца питомца",
            description = "Позволяет зарегистрировать пользователя с ролью OWNER"
    )
    public ResponseEntity<String> register(@RequestBody OwnerRequest user) {
        authService.registerPetOwner(user);
        return ResponseEntity.ok("Pet owner registered successfully");
    }

    @PostMapping("/registerSitter")
    @Operation(
            summary = "Регистрация пет-ситтера",
            description = "Позволяет зарегистрировать пользователя с ролью SITTER"
    )
    public ResponseEntity<String> register(@RequestBody SitterRequest user) {
        authService.registerPetSitter(user);
        return ResponseEntity.ok("Pet sitter registered successfully");
    }


    @PostMapping("/logout")
    @Operation(
            summary = "Выход из системы",
            description = "Позволяет пользователю выйти из системы"
    )
    public ResponseEntity<String> logout(HttpServletRequest request) {
        if (request.getSession(false) != null) {
            HttpSession session = request.getSession(false);
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout successful");
    }
}
