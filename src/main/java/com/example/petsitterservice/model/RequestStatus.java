package com.example.petsitterservice.model;

public enum RequestStatus {
    PENDING, // запрос отправлен, ожидает рассмотрения
    ACCEPTED, // запрос принят
    DECLINED, // запрос отклонен
    PERSONAL // создана персональная заявка

}
