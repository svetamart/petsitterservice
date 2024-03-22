package com.example.petsitterservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.petsitterservice.model.dto.AuthResponse;
import com.example.petsitterservice.model.dto.OwnerRequest;
import com.example.petsitterservice.model.dto.SitterRequest;
import com.example.petsitterservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name="Авторизация и аутентификация", description="Обработка запросов, связанных с аутентификацией и авторизацией пользователей в системе")
public class LoginController {

    private final AuthService authService;

    @Autowired
    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/{username}")
    @Operation(
            summary = "Вход в систему",
            description = "Позволяет залогинить пользователя"
    )
    public ResponseEntity<AuthResponse> login(
            @PathVariable @Parameter(description = "Имя пользователя") String username) {

        AuthResponse authenticationResult = authService.login(username);
        if (authenticationResult.getMessage().contains("successful")) {
            return ResponseEntity.ok(authenticationResult);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body
                    (new AuthResponse(authenticationResult.getMessage(), null, null));
        }
    }


    @PostMapping("/registerOwner")
    @Operation(
            summary = "Регистрация владельца питомца",
            description = "Позволяет зарегистрировать пользователя с ролью OWNER"
    )
    public ResponseEntity<String> register(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пользовательский запрос на регистрацию, из которого строится объект владельца питомца") OwnerRequest user) {
        authService.registerPetOwner(user);
        return ResponseEntity.ok("Pet owner registered successfully");
    }

    @PostMapping("/registerSitter")
    @Operation(
            summary = "Регистрация пет-ситтера",
            description = "Позволяет зарегистрировать пользователя с ролью SITTER"
    )
    public ResponseEntity<String> register(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пользовательский запрос на регистрацию, из которого строится объект пет-ситтера") SitterRequest user) {
        authService.registerPetSitter(user);
        return ResponseEntity.ok("Pet sitter registered successfully");
    }
}
