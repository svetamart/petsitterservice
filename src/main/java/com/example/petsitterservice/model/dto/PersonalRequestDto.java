package com.example.petsitterservice.model.dto;

/**
 * Класс для передачи данных из формы создания персональной заявки
 */
public class PersonalRequestDto {

    Long userId;
    private Long requestId;
    private String name;
    private String phone;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
