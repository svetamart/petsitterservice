package com.example.petsitterservice.model.dto;

import com.example.petsitterservice.model.PetSitter;

import java.util.List;

/**
 * Класс для отображения карточек на странице с подходящими для запроса пет-ситтерами
 */
public class SuitableSitterDto {

    private Long id;
    private String name;
    private String lastName;
    private String bio;
    private String city;
    private String experience;
    private List<String> acceptedPets;
    private boolean hasOwnPets;
    private int dailyRate;
    private int hourlyRate;

    public static SuitableSitterDto fromSitter(PetSitter sitter) {
        SuitableSitterDto petSitter = new SuitableSitterDto();
        petSitter.setId(sitter.getId());
        petSitter.setName(sitter.getName());
        petSitter.setLastName(sitter.getLastName());
        petSitter.setBio(sitter.getBio());
        petSitter.setCity(sitter.getCity());
        petSitter.setAcceptedPets(sitter.getAcceptedPets());
        petSitter.setExperience(sitter.getExperience());
        petSitter.setHasOwnPets(sitter.hasOwnPets());
        petSitter.setDailyRate((int) sitter.getDailyRate());
        petSitter.setHourlyRate((int) sitter.getHourlyRate());

        return petSitter;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    public boolean isHasOwnPets() {
        return hasOwnPets;
    }

    public void setHasOwnPets(boolean hasOwnPets) {
        this.hasOwnPets = hasOwnPets;
    }

    public int getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(int dailyRate) {
        this.dailyRate = dailyRate;
    }

    public int getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(int hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}
