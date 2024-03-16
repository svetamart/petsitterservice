package com.example.petsitterservice.model.dto;

import com.example.petsitterservice.model.PetBoardingRequest;
import com.example.petsitterservice.model.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

public class PetSitterDashboard {

    private Long id;
    private String username;

    private String name;

    private String lastName;

    private String city;

    private String bio;

    private double hourlyRate;

    private double dailyRate;

    private boolean takingNewOrders;

    private List<Review> reviews;

    private List<String> availabilityDates;


    private List<SitterPageBoardingRequest> requests = new ArrayList<>();

    public PetSitterDashboard() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    public boolean isTakingNewOrders() {
        return takingNewOrders;
    }

    public void setTakingNewOrders(boolean takingNewOrders) {
        this.takingNewOrders = takingNewOrders;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<String> getAvailabilityDates() {
        return availabilityDates;
    }

    public void setAvailabilityDates(List<String> availabilityDates) {
        this.availabilityDates = availabilityDates;
    }

    public List<SitterPageBoardingRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<SitterPageBoardingRequest> requests) {
        this.requests = requests;
    }
}
