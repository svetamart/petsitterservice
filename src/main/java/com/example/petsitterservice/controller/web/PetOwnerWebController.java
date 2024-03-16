package com.example.petsitterservice.controller.web;

import com.example.petsitterservice.model.*;
import com.example.petsitterservice.model.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/petOwner/dashboard")
public class PetOwnerWebController {
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(PetOwnerWebController.class);

    @Value("${api.url}")
    private String apiUrl;

    @Autowired
    public PetOwnerWebController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @GetMapping("/{userId}")
    // @PreAuthorize("#userId == principal.id")
    public String getUserDashboard(@PathVariable Long userId, Model model) {
        String url = "http://localhost:8080/api/users/dashboard/" + userId;

        ResponseEntity<PetOwnerDashboard> responseEntity = restTemplate.getForEntity(url, PetOwnerDashboard.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            PetOwnerDashboard user = responseEntity.getBody();
            List<Pet> pets = user.getPets();
            List<OwnerPageBoardingRequest> requests = user.getRequests();


            model.addAttribute("requests", requests != null ? requests : Collections.emptyList());

            model.addAttribute("user", user);
            model.addAttribute("userId", user.getId());
            model.addAttribute("pets", pets != null ? pets : Collections.emptyList());
            return "petOwnerPage";
        } else {
            return "error";
        }
    }

    @GetMapping("/{userId}/addPetForm")
    public String showAddPetForm(Model model, @PathVariable Long userId) {
        String url = "http://localhost:8080/api/users/dashboard/" + userId;

        ResponseEntity<PetOwner> responseEntity = restTemplate.getForEntity(url, PetOwner.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            PetOwner user = responseEntity.getBody();
            model.addAttribute("userId", user.getId());
            logger.info("add pet form called");
            logger.info(user.toString());
            model.addAttribute("pet", new PetDto());
            return "addPetForm";
        }  else {
            return "error";
        }
    }

    @PostMapping("/{userId}/addPet")
    public String addPet(@PathVariable Long userId, @ModelAttribute PetDto pet, Model model) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            pet.setUserId(userId);

            HttpEntity<PetDto> request = new HttpEntity<>(pet, headers);
            ResponseEntity<Void> response = restTemplate.exchange(
                    apiUrl + "/dashboard/" + userId + "/addPet", HttpMethod.POST, request, Void.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("successMessage", "Pet successfully added");
            } else {
                model.addAttribute("errorMessage", "Failed to add pet");
            }
        } catch (RestClientException e) {
            model.addAttribute("errorMessage", "Failed to add pet");
        }
        return "redirect:/petOwner/dashboard/" + userId;
    }

    @GetMapping("/{userId}/addRequestForm")
    public String showAddRequestForm(Model model, @PathVariable Long userId) {
        String url = "http://localhost:8080/api/users/dashboard/" + userId;

        ResponseEntity<PetOwner> responseEntity = restTemplate.getForEntity(url, PetOwner.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            PetOwner user = responseEntity.getBody();
            model.addAttribute("user", user);
            model.addAttribute("userId", user.getId());
            logger.info("add request form called");
            model.addAttribute("request", new PetBoardingRequestDto());
            return "addRequestForm";
        }  else {
            return "error";
        }
    }

    @PostMapping("{userId}/createRequest")
    public String addPetBoardingRequest(@PathVariable Long userId, @ModelAttribute PetBoardingRequestDto requestDto, Model model) {
        try {
            ResponseEntity<PetBoardingRequest> responseEntity = restTemplate.postForEntity(
                    "http://localhost:8080/api/users/dashboard/" + userId + "/createRequest",
                    requestDto,
                    PetBoardingRequest.class,
                    userId);

            if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
                PetBoardingRequest request = responseEntity.getBody();
                Long requestId = request.getId();

                return "redirect:/petOwner/dashboard/" + userId + "/boardingRequest/" + requestId + "/sitters";
            } else {
                return "error";
            }
        } catch (RestClientException e) {
            model.addAttribute("errorMessage", "Failed to add pet boarding request");
            return "error";
        }
    }

// показывает ситтеров, подходящих под реквест
    @GetMapping("/{userId}/boardingRequest/{requestId}/sitters")
    public String showSuitableSitters(Model model, @PathVariable Long userId, @PathVariable Long requestId) {
        String url = "http://localhost:8080/api/users/findSitters/" + requestId;

        ResponseEntity<List<PetSitter>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PetSitter>>() {
                }
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            List<PetSitter> sitters = responseEntity.getBody();
            model.addAttribute("requestId", requestId);
            model.addAttribute("userId", userId);
            if (sitters == null || sitters.isEmpty()) {
                model.addAttribute("personalRequest", new PersonalRequestDto());
                return "sittersNotFound";
            } else {
                model.addAttribute("sitters", sitters);
                return "suitableSittersPage";
            }
        } else {
            return "error";
        }
    }

    @PostMapping("/{userId}/chooseSitter/{requestId}/{sitterId}")
    public String chooseSitter(Model model, @PathVariable Long userId, @PathVariable Long requestId, @PathVariable Long sitterId) {
        String url = "http://localhost:8080/api/users/requests/" + requestId + "/chooseSitter/" + sitterId;
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, null, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return "redirect:/petOwner/dashboard/" + userId;
        } else {
            model.addAttribute("errorMessage", "Failed to choose pet sitter.");
            return "errorPage";
        }
    }

    @PostMapping("/{userId}/{requestId}/makePersonalRequest")
    public String makePersonalRequest(Model model, @PathVariable Long userId,
                                      @PathVariable Long requestId, @ModelAttribute PersonalRequestDto requestDTO) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            requestDTO.setUserId(userId);
            requestDTO.setRequestId(requestId);

            HttpEntity<PersonalRequestDto> request = new HttpEntity<>(requestDTO, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8080/api/users/" + userId + "/makePersonalRequest",
                    HttpMethod.POST,
                    request,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("successMessage", "Personal request submitted");
                return "redirect:/petOwner/dashboard/" + userId;
            } else {
                model.addAttribute("errorMessage", "Failed to submit personal request");
                return "error";
            }

    }


}

