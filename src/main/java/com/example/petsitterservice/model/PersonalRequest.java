package com.example.petsitterservice.model;

import jakarta.persistence.*;

@Entity
public class PersonalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private PetOwner owner;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private PetBoardingRequest request;

    @Column(nullable = false)
    private String phone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PetOwner getOwner() {
        return owner;
    }

    public void setOwner(PetOwner owner) {
        this.owner = owner;
    }

    public PetBoardingRequest getRequest() {
        return request;
    }

    public void setRequest(PetBoardingRequest request) {
        this.request = request;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
