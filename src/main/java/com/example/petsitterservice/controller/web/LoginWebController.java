package com.example.petsitterservice.controller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.petsitterservice.model.dto.AuthResponse;
import com.example.petsitterservice.model.dto.OwnerRequest;
import com.example.petsitterservice.model.dto.SitterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Controller
public class LoginWebController {
    private static final String LOGIN = "login";
    private static final String REDIRECT_LOGIN = "redirect:/login";
    private static final String ERROR = "notFound";
    private final RestTemplate restTemplate;

    @Value("${api.url}")
    private String apiUrl;

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
        model.addAttribute("username", "");
        model.addAttribute("password", "");
        return LOGIN;
    }

    @GetMapping("/success")
        public String success(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        logger.info(username);
        String url = apiUrl + "login/" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(username, headers);

        ResponseEntity<AuthResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, AuthResponse.class);
        AuthResponse response = responseEntity.getBody();

        if (response != null) {
            String role = response.getUserRole();
            Long userId = response.getUserId();
            if ("ROLE_OWNER".equals(role)) {
                return "redirect:/petOwner/dashboard/" + userId;
            } else if ("ROLE_SITTER".equals(role)) {
                return "redirect:/petSitter/dashboard/" + userId;
            } else {
                return ERROR;
            }
        } else {
            return ERROR;
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
        String url = apiUrl + "registerOwner";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OwnerRequest> requestEntity = new HttpEntity<>(user, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return REDIRECT_LOGIN;
            } else {
                logger.error("Error during registration. Please try again. Status: " + responseEntity.getStatusCodeValue());
                return ERROR;
            }
        } catch (HttpClientErrorException e) {
            logger.error("Error during registration. Please try again. Exception: " + e.getMessage());
            return ERROR;
        }
    }

    @PostMapping("/register/sitter")
    public String registerSitter(@ModelAttribute SitterRequest user, Model model) {
        String url = apiUrl +"registerSitter";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SitterRequest> requestEntity = new HttpEntity<>(user, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return REDIRECT_LOGIN;
        } else {
            logger.error("Error during registration. Please try again.");
            return ERROR;
        }
    }


//    @GetMapping ("/logout")
//    public String logout() {
//        logger.info("logout web controller");
//        String logoutUrl = "http://localhost:8080/api/logout";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
//        restTemplate.postForEntity(logoutUrl, requestEntity, String.class);
//        return REDIRECT_LOGIN;
//    }
}
