package com.example.petsitterservice.service;

import com.example.petsitterservice.model.PersonalRequest;
import com.example.petsitterservice.repository.PersonalRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalRequestService {
    private final PersonalRequestRepository requestRepository;

    @Autowired
    public PersonalRequestService(PersonalRequestRepository requestRepository){
        this.requestRepository = requestRepository;
    }

    public void addRequest (PersonalRequest request) {
        requestRepository.save(request);
    }

    public PersonalRequest getRequestById(Long id) {
        return requestRepository.findById(id).orElse(null);
    }


}
