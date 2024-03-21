package com.example.petsitterservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Класс персональной заявки
 */

@Entity
public class PersonalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private PetOwner owner;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id")
    @JsonIgnore
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
