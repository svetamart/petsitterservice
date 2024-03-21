package com.example.petsitterservice.service;

import com.example.petsitterservice.model.*;
import com.example.petsitterservice.model.dto.PetBoardingRequestDto;
import com.example.petsitterservice.model.repository.PetBoardingRequestRepository;
import com.example.petsitterservice.model.repository.PetOwnerRepository;
import com.example.petsitterservice.model.repository.PetSitterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * Сервис, отвечающий за управление запросами на передержку.
 */

@Service
public class PetBoardingRequestService {
    private final PetBoardingRequestRepository petBoardingRequestRepository;
    private final PetSitterRepository petSitterRepository;
    private final PetOwnerRepository petOwnerRepository;

    /**
     * Конструктор для создания экземпляра сервиса.
     *
     * @param petSitterRepository              Репозиторий пет-ситтеров
     * @param petOwnerRepository               Репозиторий владельцев питомцев
     * @param petBoardingRequestRepository     Репозиторий запросов на передержку
     */
    @Autowired
    public PetBoardingRequestService(PetBoardingRequestRepository petBoardingRequestRepository,
                                     PetSitterRepository petSitterRepository,
                                     PetOwnerRepository petOwnerRepository) {
        this.petBoardingRequestRepository = petBoardingRequestRepository;
        this.petOwnerRepository = petOwnerRepository;
        this.petSitterRepository = petSitterRepository;
    }

    /**
     * Создает запрос на передержку питомца.
     *
     * @param user        Пользователь, создающий запрос
     * @param pet         Питомец, для которого создается запрос
     * @param requestDto  Данные запроса
     * @return Созданный запрос на передержку питомца
     */
    public PetBoardingRequest createRequest(PetOwner user, Pet pet, PetBoardingRequestDto requestDto) {
        PetBoardingRequest request = new PetBoardingRequest();
        request.setUser(user);
        request.setPet(pet);
        request.setStartDate(requestDto.getStartDate());
        request.setEndDate(requestDto.getEndDate());
        request.setSitterExperience(requestDto.getSitterExperience());
        request.setOtherPetsAccepted(requestDto.isOtherPetsAccepted());
        request.setStatus(RequestStatus.UNPROCESSED);
        request.setComments(requestDto.getComments());
        petBoardingRequestRepository.save(request);
        user.addRequest(request);
        petOwnerRepository.save(user);
        return request;
    }

    /**
     * Находит запрос на передержку питомца по его идентификатору.
     *
     * @param requestId Идентификатор запроса на передержку питомца
     * @return Запрос на передержку питомца с указанным идентификатором или null, если запрос не найден
     */
    public PetBoardingRequest findById(Long requestId) {
        return petBoardingRequestRepository.findById(requestId).orElse(null);
    }

    /**
     * Обновляет статус запроса на передержку.
     *
     * @param id Идентификатор запроса
     * @param status Новый статус запроса
     */
    public void updateRequestStatus (Long id, RequestStatus status) {
        PetBoardingRequest request = findById(id);
        if (request != null) {
            request.setStatus(status);
            petBoardingRequestRepository.save(request);
        }
    }

    /**
     * Назначает пет-ситтера на запрос о передержке.
     *
     * @param requestId      Идентификатор запроса на передержку
     * @param sitter         Пет-ситтер
     */
    public void assignSitter (Long requestId, PetSitter sitter) {
        PetBoardingRequest request = petBoardingRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Request not found"));
        sitter.addRequest(request);
        request.setSitter(sitter);
        request.setStatus(RequestStatus.PENDING);
        petBoardingRequestRepository.save(request);
        petSitterRepository.save(sitter);
    }

    /**
     * Сохраняет запрос на передержку в репозитории.
     *
     * @param request  Запрос на передержку
     */
    public void saveRequest(PetBoardingRequest request) {
        petBoardingRequestRepository.save(request);
    }
}