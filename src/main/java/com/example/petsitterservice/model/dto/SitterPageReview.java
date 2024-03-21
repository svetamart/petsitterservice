package com.example.petsitterservice.model.dto;

import com.example.petsitterservice.model.Review;

/**
 * Класс для отображения отзывов  в личном кабинете пет-ситтера
 */


public class SitterPageReview {

    private String userName;
    private int rating;
    private String petName;
    private String startDate;
    private String endDate;
    private String message;


    public static SitterPageReview fromReview(Review review) {
        SitterPageReview sitterPageReview = new SitterPageReview();
        sitterPageReview.setPetName(review.getRequest().getPet().getName());
        sitterPageReview.setUserName(review.getRequest().getUser().getName());
        sitterPageReview.setRating(Integer.parseInt(review.getRating()));
        sitterPageReview.setMessage(review.getMessage());
        sitterPageReview.setStartDate(review.getRequest().getStartDate());
        sitterPageReview.setEndDate(review.getRequest().getEndDate());

        return sitterPageReview;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
