package com.example.petsitterservice.controller.web;

import com.example.petsitterservice.model.*;
import com.example.petsitterservice.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/petSitter/dashboard")
public class PetSitterWebController {

    private static final String ERROR = "error";
    private static final String USER_ID = "userId";
    private static final String REDIRECT_DASHBOARD = "redirect:/petSitter/dashboard/";
    private static final String API_DASHBOARD = "http://localhost:8080/api/petSitters/dashboard/";

    private final RestTemplate restTemplate;


    @Autowired
    public PetSitterWebController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @GetMapping("/{userId}")
    @PreAuthorize("#userId == principal.id")
    public String getUserDashboard(@PathVariable Long userId, Model model) {
        String url = API_DASHBOARD + userId;

        ResponseEntity<PetSitterDashboard> responseEntity = restTemplate.getForEntity(url, PetSitterDashboard.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            PetSitterDashboard user = responseEntity.getBody();
            if (user != null) {
                List<SitterPageBoardingRequest> sitterPageRequests = user.getRequests();
                List<SitterPageReview> sitterReviews = user.getReviews();

                model.addAttribute("user", user);
                model.addAttribute("sitterPageRequests", sitterPageRequests != null ? sitterPageRequests : Collections.emptyList());
                model.addAttribute("sitterPageReviews", sitterReviews != null ? sitterReviews : Collections.emptyList());
            }
            return "petSitterPage";
        } else {
            return ERROR;
        }
    }

    @GetMapping("/{userId}/changeNewOrders")
    public String showChangeNewOrdersPage(@PathVariable("userId") Long userId, Model model) {
        String url = API_DASHBOARD + userId;

        ResponseEntity<PetSitter> responseEntity = restTemplate.getForEntity(url, PetSitter.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            PetSitter user = responseEntity.getBody();
            if (user != null) {
                model.addAttribute(USER_ID, user.getId());
                model.addAttribute("newOrders", user.isTakingNewOrders());
            }
            return "changeNewOrders";
        }  else {
            return ERROR;
        }
    }

    @PostMapping("/changeNewOrders")
    public String changeNewOrdersStatus(@RequestParam("userId") Long userId, @RequestParam("newOrders") boolean newOrders) {
        String url = "http://localhost:8080/api/petSitters/toggleNewOrders/" + userId + "?newOrders=" + newOrders;
        restTemplate.postForEntity(url, null, Void.class);
        return REDIRECT_DASHBOARD + userId;
    }

    @GetMapping("/{userId}/changeAvailability")
    public String showChangeAvailabilityPage(@PathVariable("userId") Long userId, Model model) {
        String url =API_DASHBOARD + userId;

        ResponseEntity<PetSitter> responseEntity = restTemplate.getForEntity(url, PetSitter.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            PetSitter user = responseEntity.getBody();
            if (user!= null) {
                AvailabilityRequest availability = new AvailabilityRequest();
                model.addAttribute(USER_ID, user.getId());
                model.addAttribute("availability", availability);
            }
            return "changeAvailability";
        }  else {
            return ERROR;
        }
    }

    @PostMapping("/changeAvailability")
    public String changeAvailability(@RequestParam("userId") Long userId, @ModelAttribute AvailabilityRequest request) {
        String url = "http://localhost:8080/api/petSitters/changeAvailability/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AvailabilityRequest> requestEntity = new HttpEntity<>(request, headers);

        restTemplate.postForEntity(url, requestEntity, Void.class);

        return REDIRECT_DASHBOARD + userId;
    }

    @PostMapping("/{userId}/acceptRequest/{requestId}")
    public String acceptRequest(@PathVariable Long userId, @PathVariable Long requestId, RedirectAttributes redirectAttributes, Model model) {
        String url = "http://localhost:8080/api/petSitters/acceptRequest/" + requestId;
        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            redirectAttributes.addFlashAttribute("message", "Вы приняли заявку");
            return REDIRECT_DASHBOARD + userId;
        } else {
            redirectAttributes.addFlashAttribute(ERROR, "Ошибка");
            model.addAttribute(USER_ID, userId);
            return "cannotAcceptRequestPage";
        }
    }

    @PostMapping("/{userId}/declineRequest/{requestId}")
    public String declineRequest(@PathVariable Long userId, @PathVariable Long requestId, RedirectAttributes redirectAttributes) {
        String url = "http://localhost:8080/api/petSitters/declineRequest/" + requestId;
        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            redirectAttributes.addFlashAttribute("message", "Заявка отклонена");
        } else {
            redirectAttributes.addFlashAttribute(ERROR, "Failed to decline request");
        }
        return REDIRECT_DASHBOARD + userId;
    }

}




