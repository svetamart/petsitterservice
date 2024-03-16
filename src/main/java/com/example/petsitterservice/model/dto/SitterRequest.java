package com.example.petsitterservice.model.dto;


import java.util.ArrayList;
import java.util.List;

public class SitterRequest {
    private String username;
    private String password;
    private String name;
    private String lastName;
    private String city;
    private String experience;
    private List<String> acceptedPets = new ArrayList<>();
    private List<String> acceptedDogSizes = new ArrayList<>();
    private boolean hasOwnPets;
    private String bio;
    private String phone;
    private double hourlyRate;
    private double dailyRate;



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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public List<String> getAcceptedPets() {
        return acceptedPets;
    }

    public void setAcceptedPets(List<String> acceptedPets) {
        this.acceptedPets = acceptedPets;
    }

    public List<String> getAcceptedDogSizes() {
        return acceptedDogSizes;
    }

    public void setAcceptedDogSizes(List<String> acceptedDogSizes) {
        this.acceptedDogSizes = acceptedDogSizes;
    }

    public boolean isHasOwnPets() {
        return hasOwnPets;
    }

    public void setHasOwnPets(boolean hasOwnPets) {
        this.hasOwnPets = hasOwnPets;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getRole() {
        return "ROLE_SITTER";
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }
}


