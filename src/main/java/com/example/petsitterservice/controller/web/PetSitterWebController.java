package com.example.petsitterservice.controller.web;

import com.example.petsitterservice.model.*;
import com.example.petsitterservice.model.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    private static final String ERROR = "notFound";
    private static final String ACCESS_DENIED = "accessDenied";
    private static final String USER_ID = "userId";
    private static final String REDIRECT_DASHBOARD = "redirect:/petSitter/dashboard/";
    private static final String DASHBOARD = "petSitters/dashboard/";

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(PetSitterWebController.class);

    @Value("${api.url}")
    private String apiUrl;


    @Autowired
    public PetSitterWebController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @GetMapping("/{userId}")
    public String getUserDashboard(@PathVariable Long userId, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String url = apiUrl + DASHBOARD + userId;

        ResponseEntity<PetSitterDashboard> responseEntity = restTemplate.getForEntity(url, PetSitterDashboard.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            PetSitterDashboard user = responseEntity.getBody();
            if (user != null) {
                String username;
                if (principal instanceof UserDetails) {
                    username = ((UserDetails) principal).getUsername();
                } else {
                    username = principal.toString();
                }
                if (username.equals(user.getUsername())) {

                    List<SitterPageBoardingRequest> sitterPageRequests = user.getRequests();
                    List<SitterPageReview> sitterReviews = user.getReviews();

                    model.addAttribute("user", user);
                    model.addAttribute("sitterPageRequests", sitterPageRequests != null ? sitterPageRequests : Collections.emptyList());
                    model.addAttribute("sitterPageReviews", sitterReviews != null ? sitterReviews : Collections.emptyList());
                return "petSitterPage";
                } else {
                    logger.error("Trying to access another user's page");
                    return ACCESS_DENIED;
                }
            } else {
                logger.error("User is null");
                return ACCESS_DENIED;
            }
        } else {
            return ERROR;
        }
    }

    @GetMapping("/{userId}/changeNewOrders")
    public String showChangeNewOrdersPage(@PathVariable("userId") Long userId, Model model) {
        String url = apiUrl + DASHBOARD + userId;

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
        String url = apiUrl + "petSitters/toggleNewOrders/" + userId + "?newOrders=" + newOrders;
        restTemplate.postForEntity(url, null, Void.class);
        return REDIRECT_DASHBOARD + userId;
    }

    @GetMapping("/{userId}/changeAvailability")
    public String showChangeAvailabilityPage(@PathVariable("userId") Long userId, Model model) {
        String url = apiUrl + DASHBOARD + userId;

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
        String url = apiUrl + "petSitters/changeAvailability/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AvailabilityRequest> requestEntity = new HttpEntity<>(request, headers);

        restTemplate.postForEntity(url, requestEntity, Void.class);

        return REDIRECT_DASHBOARD + userId;
    }

    @PostMapping("/{userId}/acceptRequest/{requestId}")
    public String acceptRequest(@PathVariable Long userId, @PathVariable Long requestId, RedirectAttributes redirectAttributes, Model model) {
        String url = apiUrl +"petSitters/acceptRequest/" + requestId;
        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            logger.info("Request accepted");
            return REDIRECT_DASHBOARD + userId;
        } else {
            logger.error("Error while trying to accept request");
            model.addAttribute(USER_ID, userId);
            return "cannotAcceptRequestPage";
        }
    }

    @PostMapping("/{userId}/declineRequest/{requestId}")
    public String declineRequest(@PathVariable Long userId, @PathVariable Long requestId, RedirectAttributes redirectAttributes) {
        String url = apiUrl +"petSitters/declineRequest/" + requestId;
        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            logger.info("Request declined");
            return REDIRECT_DASHBOARD + userId;
        } else {
            logger.error("Error while trying to decline request");
            return ERROR;
        }
    }
}




