package com.example.petsitterservice.model.dto;

/**
 * Класс для передачи данных при аутентификации пользователя.
 * Содержит сообщение, роль пользователя и идентификатор пользователя.
 */

public class AuthResponse {
    private String message;
    private String userRole;
    private Long userId;

    public AuthResponse(String message, String userRole, Long userId) {
        this.message = message;
        this.userRole = userRole;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
