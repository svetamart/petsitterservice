package com.example.petsitterservice.model.dto;

public class PetBoardingRequestDto {

    private Long userId;
    private Long petId;
    private String startDate;
    private String endDate;
    private boolean otherPetsAccepted;
    private String sitterExperience;

    private String comments;


    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isOtherPetsAccepted() {
        return otherPetsAccepted;
    }

    public void setOtherPetsAccepted(boolean otherPetsAccepted) {
        this.otherPetsAccepted = otherPetsAccepted;
    }

    public String getSitterExperience() {
        return sitterExperience;
    }

    public void setSitterExperience(String sitterExperience) {
        this.sitterExperience = sitterExperience;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

