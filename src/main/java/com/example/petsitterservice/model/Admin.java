package com.example.petsitterservice.model;

public class Admin {

    private String username;
    private String password;
    private final String role = "ROLE_ADMIN";

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
        return role;
    }
}
