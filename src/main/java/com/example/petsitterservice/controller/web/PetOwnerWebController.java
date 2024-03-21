package com.example.petsitterservice.controller.web;

import com.example.petsitterservice.model.*;
import com.example.petsitterservice.model.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/petOwner/dashboard")
public class PetOwnerWebController {

    private static final String DASHBOARD = "users/dashboard/";
    private static final String ERROR = "notFound";
    private static final String ACCESS_DENIED = "accessDenied";
    private static final String USER_ID = "userId";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String SUCCESS_MESSAGE = "successMessage";
    private static final String REDIRECT_DASHBOARD = "redirect:/petOwner/dashboard/";

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(PetOwnerWebController.class);

    @Value("${api.url}")
    private String apiUrl;

    @Autowired
    public PetOwnerWebController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @GetMapping("/{userId}")
    public String getUserDashboard(@PathVariable Long userId, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String url = apiUrl + DASHBOARD + userId;

        ResponseEntity<PetOwnerDashboard> responseEntity = restTemplate.getForEntity(url, PetOwnerDashboard.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            PetOwnerDashboard user = responseEntity.getBody();

            String username;
            if (principal instanceof UserDetails) {
                username = ((UserDetails)principal).getUsername();
            } else {
                username = principal.toString();
            }
            if (username.equals(user.getUsername())) {

                List<Pet> pets = user.getPets();
                List<OwnerPageBoardingRequest> requests = user.getRequests();
                LocalDate currentDate = LocalDate.now();

                for (OwnerPageBoardingRequest request : requests) {
                    LocalDate endDate = LocalDate.parse(request.getEndDate());
                    boolean isDatePastOrPresent = !endDate.isAfter(currentDate) || endDate.isEqual(currentDate);
                    request.setAvailableToReview(isDatePastOrPresent);
                }

                model.addAttribute("requests", requests != null ? requests : Collections.emptyList());

                model.addAttribute("user", user);
                model.addAttribute(USER_ID, user.getId());
                model.addAttribute("pets", pets != null ? pets : Collections.emptyList());
                return "petOwnerPage";
            } else {
               return ACCESS_DENIED;
            }
        } else {
            return ERROR;
        }
    }

    @GetMapping("/{userId}/addPetForm")
    public String showAddPetForm(Model model, @PathVariable Long userId) {
        String url = apiUrl + DASHBOARD + userId;

        ResponseEntity<PetOwner> responseEntity = restTemplate.getForEntity(url, PetOwner.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            PetOwner user = responseEntity.getBody();
            model.addAttribute(USER_ID, user.getId());
            model.addAttribute("pet", new PetDto());
            return "addPetForm";
        }  else {
            return ERROR;
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
                    apiUrl + DASHBOARD + userId + "/addPet", HttpMethod.POST, request, Void.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Pet successfully added");
            } else {
                logger.error("Failed to add pet");
            }
        } catch (RestClientException e) {
            logger.error(e.getMessage());
        }
        return REDIRECT_DASHBOARD + userId;
    }

    @GetMapping("/{userId}/addRequestForm")
    public String showAddRequestForm(Model model, @PathVariable Long userId) {
        String url = apiUrl + DASHBOARD + userId;

        ResponseEntity<PetOwner> responseEntity = restTemplate.getForEntity(url, PetOwner.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            PetOwner user = responseEntity.getBody();
            model.addAttribute("user", user);
            model.addAttribute(USER_ID, user.getId());
            logger.info("add request form called");
            model.addAttribute("request", new PetBoardingRequestDto());
            return "addRequestForm";
        }  else {
            return ERROR;
        }
    }

    @PostMapping("{userId}/createRequest")
    public String addPetBoardingRequest(@PathVariable Long userId, @ModelAttribute PetBoardingRequestDto requestDto, Model model) {
        try {
            ResponseEntity<PetBoardingRequest> responseEntity = restTemplate.postForEntity(
                    apiUrl + DASHBOARD + userId + "/createRequest",
                    requestDto,
                    PetBoardingRequest.class,
                    userId);

            if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
                PetBoardingRequest request = responseEntity.getBody();
                Long requestId = request.getId();

                return REDIRECT_DASHBOARD + userId + "/boardingRequest/" + requestId + "/sitters";
            } else {
                logger.error("Failed to add pet boarding request");
                return ERROR;
            }
        } catch (RestClientException e) {
            logger.error(e.getMessage());
            return ERROR;
        }
    }

    @GetMapping("/{userId}/boardingRequest/{requestId}/sitters")
    public String showSuitableSitters(Model model, @PathVariable Long userId, @PathVariable Long requestId) {
        String url = apiUrl + "users/findSitters/" + requestId;

        ResponseEntity<List<SuitableSitterDto>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SuitableSitterDto>>() {
                }
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            List<SuitableSitterDto> sitters = responseEntity.getBody();
            model.addAttribute("requestId", requestId);
            model.addAttribute(USER_ID, userId);
            if (sitters == null || sitters.isEmpty()) {
                model.addAttribute("personalRequest", new PersonalRequestDto());
                return "sittersNotFound";
            } else {
                model.addAttribute("sitters", sitters);
                return "suitableSittersPage";
            }
        } else {
            return ERROR;
        }
    }

    @PostMapping("/{userId}/chooseSitter/{requestId}/{sitterId}")
    public String chooseSitter(Model model, @PathVariable Long userId, @PathVariable Long requestId, @PathVariable Long sitterId) {
        String url = apiUrl + "users/requests/" + requestId + "/chooseSitter/" + sitterId;
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, null, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return REDIRECT_DASHBOARD + userId;
        } else {
            logger.error("Failed to choose pet sitter.");
            return ERROR;
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
                    apiUrl + "users/" + userId + "/makePersonalRequest",
                    HttpMethod.POST,
                    request,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Personal request submitted");
                return REDIRECT_DASHBOARD + userId;
            } else {
                logger.error("Failed to submit personal request");
                return ERROR;
            }
    }

    @GetMapping("/{userId}/addReviewForm/{requestId}")
    public String showAddReviewForm(Model model, @PathVariable Long requestId, @PathVariable Long userId) {
        model.addAttribute(USER_ID, userId);
        model.addAttribute("request", requestId);
        model.addAttribute("review", new ReviewDto());
        return "addReviewForm";
    }

    @PostMapping("/{userId}/{requestId}/addReview")
    public String addReview(Model model, @ModelAttribute ReviewDto reviewDto, @PathVariable Long userId, @PathVariable Long requestId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = apiUrl + DASHBOARD +"addReview";

        reviewDto.setRequestId(requestId);

        HttpEntity<ReviewDto> requestEntity = new HttpEntity<>(reviewDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Review successfully added");
                return REDIRECT_DASHBOARD + userId;
            } else {
                logger.error("Failed adding review");
                return ERROR;
            }
    }




}

