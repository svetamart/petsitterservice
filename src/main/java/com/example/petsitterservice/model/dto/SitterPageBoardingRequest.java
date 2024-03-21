package com.example.petsitterservice.model.dto;

import com.example.petsitterservice.model.PetBoardingRequest;
import com.example.petsitterservice.model.RequestStatus;

/**
 * Класс для отображения запросов на передержку в личном кабинете пет-ситтера
 */

public class SitterPageBoardingRequest {

    private Long id;

    private String userName;
    private PetForSitterPageRequest pet;

    private String startDate;
    private String endDate;
    private boolean otherPetsAccepted;
    private String comments;
    private RequestStatus status;


    public static SitterPageBoardingRequest fromPetBoardingRequest(PetBoardingRequest petBoardingRequest) {
        SitterPageBoardingRequest sitterPageBoardingRequest = new SitterPageBoardingRequest();
        sitterPageBoardingRequest.setId(petBoardingRequest.getId());
        sitterPageBoardingRequest.setUserName(petBoardingRequest.getUser().getName());

        PetForSitterPageRequest petForSitterPageRequest = PetForSitterPageRequest.fromPet(petBoardingRequest.getPet());
        sitterPageBoardingRequest.setPet(petForSitterPageRequest);

        sitterPageBoardingRequest.setStartDate(petBoardingRequest.getStartDate());
        sitterPageBoardingRequest.setEndDate(petBoardingRequest.getEndDate());
        sitterPageBoardingRequest.setOtherPetsAccepted(petBoardingRequest.isOtherPetsAccepted());
        sitterPageBoardingRequest.setComments(petBoardingRequest.getComments());
        sitterPageBoardingRequest.setStatus(petBoardingRequest.getStatus());

        return sitterPageBoardingRequest;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public PetForSitterPageRequest getPet() {
        return pet;
    }

    public void setPet(PetForSitterPageRequest pet) {
        this.pet = pet;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
