package com.example.petsitterservice.model;

/**
 * Класс администратора
 */
public class Admin {

    private String username;
    private String password;
    private static final String ROLE = "ROLE_ADMIN";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return ROLE;
    }
}
