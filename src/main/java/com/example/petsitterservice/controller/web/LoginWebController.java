package com.example.petsitterservice.controller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.petsitterservice.model.dto.AuthResponse;
import com.example.petsitterservice.model.dto.LoginRequest;
import com.example.petsitterservice.model.dto.OwnerRequest;
import com.example.petsitterservice.model.dto.SitterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationTargetException;

@Controller
public class LoginWebController {
    private static final String LOGIN = "login";
    private static final String REDIRECT_LOGIN = "redirect:/login";
    private static final String ERROR = "error";
    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(LoginWebController.class);


    @Autowired
    public LoginWebController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/main")
    public String mainPage(Model model) {
        return "main";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return LOGIN;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest user, Model model){
        String apiUrl = "http://localhost:8080/api/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(user, headers);

            ResponseEntity<AuthResponse> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, AuthResponse.class);
            AuthResponse response = responseEntity.getBody();
            if (response != null) {
                String role = response.getUserRole();
                Long userId = response.getUserId();
                if ("ROLE_OWNER".equals(role)) {
                    return "redirect:/petOwner/dashboard/" + userId;
                } else if ("ROLE_SITTER".equals(role)) {
                    return "redirect:/petSitter/dashboard/" + userId;
                } else {
                    model.addAttribute(ERROR, "Unknown role");
                    return LOGIN;
                }
            } else {
            model.addAttribute(ERROR, "Invalid response from server");
            return LOGIN;
        }
    }

    @GetMapping("/register/owner")
    public String registerOwnerForm(Model model) {
        model.addAttribute("ownerRequest", new OwnerRequest());
        return "registerOwnerForm";
    }

    @GetMapping("/register/sitter")
    public String registerSitterForm(Model model) {
        model.addAttribute("sitterRequest", new SitterRequest());
        return "registerSitterForm";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        return "registerPage";
    }

    @PostMapping("/register/owner")
    public String registerOwner(@ModelAttribute OwnerRequest user, Model model) {
        String apiUrl = "http://localhost:8080/api/registerOwner";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OwnerRequest> requestEntity = new HttpEntity<>(user, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return REDIRECT_LOGIN;
            } else {
                model.addAttribute(ERROR, "Error during registration. Please try again. Status: " + responseEntity.getStatusCodeValue());
                return "redirect:/register/owner";
            }
        } catch (HttpClientErrorException e) {
            model.addAttribute(ERROR, "Error during registration. Please try again. Exception: " + e.getMessage());
            return "redirect:/register/owner";
        }
    }

    @PostMapping("/register/sitter")
    public String registerSitter(@ModelAttribute SitterRequest user, Model model) {
        String apiUrl = "http://localhost:8080/api/registerSitter";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SitterRequest> requestEntity = new HttpEntity<>(user, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return REDIRECT_LOGIN;
        } else {
            model.addAttribute(ERROR, "Error during registration. Please try again.");
            return "redirect:/register/sitter";
        }
    }


    @GetMapping ("/logout")
    public String logout() {
        logger.info("logout web controller");
        String logoutUrl = "http://localhost:8080/api/logout";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        restTemplate.postForEntity(logoutUrl, requestEntity, String.class);
        return REDIRECT_LOGIN;
    }
}
