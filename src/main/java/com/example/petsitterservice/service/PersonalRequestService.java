package com.example.petsitterservice.service;

import com.example.petsitterservice.model.PersonalRequest;
import com.example.petsitterservice.model.repository.PersonalRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис, отвечающий за операции с персональными заявками.
 */

@Service
public class PersonalRequestService {
    private final PersonalRequestRepository requestRepository;

    /**
     * Конструктор для создания экземпляра класса.
     *
     * @param requestRepository   Репозиторий персональных заявок
     */
    @Autowired
    public PersonalRequestService(PersonalRequestRepository requestRepository){
        this.requestRepository = requestRepository;
    }

    /**
     * Добавляет персональный запрос в репозиторий.
     *
     * @param request Персональный запрос
     */
    public void addRequest (PersonalRequest request) {
        requestRepository.save(request);
    }

    /**
     * Получает персональный запрос по идентификатору.
     *
     * @param id Идентификатор персонального запроса
     * @return Персональный запрос с указанным идентификатором или null, если не найден
     */
    public PersonalRequest getRequestById(Long id) {
        return requestRepository.findById(id).orElse(null);
    }


}
