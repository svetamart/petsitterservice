package com.example.petsitterservice.model.dto;

/**
 * Класс для передачи данных из формы создания отзыва
 */
public class ReviewDto {
    private String message;
    private String rating;
    private Long requestId;

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

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
}
