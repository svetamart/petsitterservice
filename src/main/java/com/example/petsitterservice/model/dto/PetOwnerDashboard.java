package com.example.petsitterservice.model.dto;

import com.example.petsitterservice.model.Pet;

import java.util.ArrayList;
import java.util.List;

public class PetOwnerDashboard {

    private Long id;
    private String username;
    private String name;
    private String lastName;
    private String city;
    private List<Pet> pets = new ArrayList<>();
    private List<OwnerPageBoardingRequest> requests = new ArrayList<>();

    public PetOwnerDashboard() {
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

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public List<OwnerPageBoardingRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<OwnerPageBoardingRequest> requests) {
        this.requests = requests;
    }
}
