package com.example.petsitterservice.model;


import jakarta.persistence.*;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private String rating;

    @OneToOne
    @JoinColumn(name = "request_id")
    private PetBoardingRequest request;

    public Review() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PetBoardingRequest getRequest() {
        return request;
    }

    public void setRequest(PetBoardingRequest request) {
        this.request = request;
    }
}
