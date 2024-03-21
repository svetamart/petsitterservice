package com.example.petsitterservice.service;

import com.example.petsitterservice.model.*;
import com.example.petsitterservice.model.dto.PetBoardingRequestDto;
import com.example.petsitterservice.model.repository.PetBoardingRequestRepository;
import com.example.petsitterservice.model.repository.PetOwnerRepository;
import com.example.petsitterservice.model.repository.PetSitterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class PetBoardingRequestService {
    private final PetBoardingRequestRepository petBoardingRequestRepository;
    private final PetSitterRepository petSitterRepository;
    private final PetOwnerRepository petOwnerRepository;

    @Autowired
    public PetBoardingRequestService(PetBoardingRequestRepository petBoardingRequestRepository,
                                     PetSitterRepository petSitterRepository,
                                     PetOwnerRepository petOwnerRepository) {
        this.petBoardingRequestRepository = petBoardingRequestRepository;
        this.petOwnerRepository = petOwnerRepository;
        this.petSitterRepository = petSitterRepository;
    }

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

    public PetBoardingRequest findById(Long requestId) {
        return petBoardingRequestRepository.findById(requestId).orElse(null);
    }

    public void updateRequestStatus (Long id, RequestStatus status) {
        PetBoardingRequest request = findById(id);
        if (request != null) {
            request.setStatus(status);
            petBoardingRequestRepository.save(request);
        }
    }

    public void assignSitter (Long requestId, PetSitter sitter) {
        PetBoardingRequest request = petBoardingRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Request not found"));
        sitter.addRequest(request);
        request.setSitter(sitter);
        request.setStatus(RequestStatus.PENDING);
        petBoardingRequestRepository.save(request);
        petSitterRepository.save(sitter);
    }

    public PetBoardingRequest getRequestById(Long requestId) {
        return petBoardingRequestRepository.findById(requestId).orElse(null);
    }

    public void saveRequest(PetBoardingRequest request) {
        petBoardingRequestRepository.save(request);
    }
}