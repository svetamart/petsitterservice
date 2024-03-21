package com.example.petsitterservice.model;

import jakarta.persistence.*;


@Entity
public class PetBoardingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_owner_id")
    private PetOwner user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_sitter_id")
    private PetSitter sitter;

    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String endDate;

    @Column(nullable = false)
    private boolean otherPetsAccepted;

    @Column(nullable = false)
    private String sitterExperience;

    @Column(nullable = true)
    private String comments;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(nullable = true)
    private boolean reviewed;

    public PetOwner getUser() {
        return user;
    }

    public void setUser(PetOwner user) {
        this.user = user;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
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

    public String getSitterExperience() {
        return sitterExperience;
    }

    public void setSitterExperience(String sitterExperience) {
        this.sitterExperience = sitterExperience;
    }

    public PetSitter getSitter() {
        return sitter;
    }

    public void setSitter(PetSitter sitter) {
        this.sitter = sitter;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }
}
