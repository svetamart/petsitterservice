package com.example.petsitterservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DTO для добавления дат в календарь пет-ситтера
 */

public class AvailabilityRequest {
    @JsonProperty("availableDates")
    private String availableDates;

    public String getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(String availableDates) {
        this.availableDates = availableDates;
    }
}
