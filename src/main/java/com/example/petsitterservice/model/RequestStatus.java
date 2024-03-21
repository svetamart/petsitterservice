package com.example.petsitterservice.model;

/**
 * Класс с перечислениями возможных статусов запросов на передержку
 */

public enum RequestStatus {
    PENDING, // запрос отправлен, ожидает рассмотрения
    ACCEPTED, // запрос принят
    DECLINED, // запрос отклонен
    PERSONAL, // создана персональная заявка
    UNPROCESSED // заявка создана, но не обработана
}
