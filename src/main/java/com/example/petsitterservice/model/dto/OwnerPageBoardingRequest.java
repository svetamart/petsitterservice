package com.example.petsitterservice.model.dto;

import com.example.petsitterservice.model.PetBoardingRequest;
import com.example.petsitterservice.model.RequestStatus;

public class OwnerPageBoardingRequest {

    private Long requestId;
    private String petName;
    private String sitterName;
    private String sitterPhone;
    private String startDate;
    private String endDate;
    private boolean otherPetsAccepted;
    private String sitterExperience;
    private String comments;
    private RequestStatus status;

    private boolean availableToReview;

    private boolean hasReview;


    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getSitterName() {
        return sitterName;
    }

    public void setSitterName(String sitterName) {
        this.sitterName = sitterName;
    }

    public String getSitterPhone() {
        return sitterPhone;
    }

    public void setSitterPhone(String sitterPhone) {
        this.sitterPhone = sitterPhone;
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

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public boolean isAvailableToReview() {
        return availableToReview;
    }

    public void setAvailableToReview(boolean availableToReview) {
        this.availableToReview = availableToReview;
    }

    public boolean getHasReview() {
        return hasReview;
    }

    public void setHasReview(boolean hasReview) {
        this.hasReview = hasReview;
    }

    @Override
    public String toString() {
        return "OwnerPageBoardingRequest{" +
                "availableToReview=" + availableToReview +
                ", hasReview=" + hasReview +
                '}';
    }

    public static OwnerPageBoardingRequest fromPetBoardingRequest(PetBoardingRequest petBoardingRequest) {
        OwnerPageBoardingRequest ownerPageBoardingRequest = new OwnerPageBoardingRequest();
        ownerPageBoardingRequest.setPetName(petBoardingRequest.getPet().getName());
        ownerPageBoardingRequest.setSitterName(petBoardingRequest.getSitter() != null ? petBoardingRequest.getSitter().getName() : "--");
        ownerPageBoardingRequest.setStartDate(petBoardingRequest.getStartDate());
        ownerPageBoardingRequest.setEndDate(petBoardingRequest.getEndDate());
        ownerPageBoardingRequest.setOtherPetsAccepted(petBoardingRequest.isOtherPetsAccepted());
        ownerPageBoardingRequest.setSitterExperience(petBoardingRequest.getSitterExperience());
        ownerPageBoardingRequest.setComments(petBoardingRequest.getComments());
        ownerPageBoardingRequest.setStatus(petBoardingRequest.getStatus());
        ownerPageBoardingRequest.setRequestId(petBoardingRequest.getId());
        ownerPageBoardingRequest.setHasReview(petBoardingRequest.isReviewed());

        if (petBoardingRequest.getStatus() == RequestStatus.ACCEPTED) {
            ownerPageBoardingRequest.setSitterPhone(petBoardingRequest.getSitter().getPhone());
        } else {
            if (petBoardingRequest.getSitter() == null) {
                ownerPageBoardingRequest.setSitterPhone("--");
            } else {
                String phoneNumber = petBoardingRequest.getSitter().getPhone();
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    String substring = phoneNumber.substring(phoneNumber.length() - 4);
                    String maskedPhoneNumber = substring.replaceAll("\\d", "*") + substring;
                    ownerPageBoardingRequest.setSitterPhone(maskedPhoneNumber);
                }
            }
        }

        return ownerPageBoardingRequest;
    }
}
