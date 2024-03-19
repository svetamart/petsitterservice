package com.example.petsitterservice.service;

import com.example.petsitterservice.utility.CustomAuthenticationManager;
import com.example.petsitterservice.model.*;
import com.example.petsitterservice.model.dto.AuthResponse;
import com.example.petsitterservice.model.dto.OwnerRequest;
import com.example.petsitterservice.model.dto.SitterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final PetServiceMainFacadeImpl userService;
    private final CustomAuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    public AuthService(PetServiceMainFacadeImpl userService, CustomAuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse login(String username, String password) {
        try {
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            if (authentication != null && authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);

                PetOwner petOwner = userService.getUserByUsername(username);
                PetSitter petSitter = null;

                if (petOwner == null) {
                    petSitter = userService.getSitterByUsername(username);
                }

                if (petOwner != null || petSitter != null) {
                    boolean accountEnabled = (petOwner != null) ? petOwner.isEnabled() : petSitter.isEnabled();

                    if (accountEnabled) {
                        Long userId = (petOwner != null) ? petOwner.getId() : petSitter.getId();
                        String userRole = authentication.getAuthorities().stream()
                                .findFirst()
                                .map(GrantedAuthority::getAuthority)
                                .orElse("");

                        return new AuthResponse("Authentication successful", userRole, userId);
                    } else {
                        logger.error("Authentication failed: User account is not enabled");
                        return new AuthResponse("Authentication failed: User account is not enabled", null, null);
                    }
                } else {
                    logger.error("Authentication failed: User not found");
                    return new AuthResponse("Authentication failed: User not authenticated", null, null);
                }
            }
        } catch (AuthenticationException e) {
            return new AuthResponse("Authentication failed: " + e.getMessage(), null, null);
        }
        return new AuthResponse("Authentication failed: Unexpected error", null, null);
    }

    public boolean isAuthorizedToAccessDashboard(Long userId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loggedInUserId = userService.getUserByUsername(userDetails.getUsername()).getId();
        return userId.equals(loggedInUserId);
    }

    public void registerPetOwner(OwnerRequest user) {
        PetOwner newUser = new PetOwner();
        newUser.setName(user.getName());
        newUser.setLastName(user.getLastName());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setCity(user.getCity());
        newUser.setRole(user.getRole());
        newUser.setAccountEnabled(true);

        userService.registerPetOwner(newUser);
    }

    public void registerPetSitter(SitterRequest user) {
        PetSitter newUser = new PetSitter();
        newUser.setName(user.getName());
        newUser.setLastName(user.getLastName());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setCity(user.getCity());
        newUser.setRole(user.getRole());
        newUser.setExperience(user.getExperience());
        newUser.setAcceptedPets(user.getAcceptedPets());
        newUser.setAcceptedDogSizes(user.getAcceptedDogSizes());
        newUser.setHasOwnPets(user.isHasOwnPets());
        newUser.setBio(user.getBio());
        newUser.setPhone(user.getPhone());
        newUser.setHourlyRate(user.getHourlyRate());
        newUser.setDailyRate(user.getDailyRate());
        newUser.setAccountEnabled(true);

        userService.registerPetSitter(newUser);
    }
}
