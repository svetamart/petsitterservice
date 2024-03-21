package com.example.petsitterservice.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Класс Отзыва
 */
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private String rating;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id")
    @JsonIgnore
    private PetBoardingRequest request;

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
