package com.example.petsitterservice.service;

import com.example.petsitterservice.model.*;
import com.example.petsitterservice.model.dto.AuthResponse;
import com.example.petsitterservice.model.dto.OwnerRequest;
import com.example.petsitterservice.model.dto.SitterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис, отвечающий за авторизацию и аутентификацию пользователей.
 */

@Service
public class AuthService {
    private final PetServiceMainFacadeImpl userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);


    /**
     * Конструктор для класса AuthService.
     * @param userService       Фасадный сервис для основных операций с питомцами и пользователями.
     */
    @Autowired
    public AuthService(PetServiceMainFacadeImpl userService) {
        this.userService = userService;
    }

    /**
     * Метод для аутентификации пользователя по его имени.
     *
     * @param username Имя пользователя для аутентификации.
     * @return Объект класса AuthResponse, содержащий идентификатор пользователя,
     *         его роль и информацию об успешной или неудачной аутентификации.
     */
    public AuthResponse login(String username) {
        PetOwner petOwner = userService.getUserByUsername(username);
        PetSitter petSitter = null;

        if (petOwner == null) {
            petSitter = userService.getSitterByUsername(username);
        }

        if (petOwner != null || petSitter != null) {
            boolean accountEnabled = (petOwner != null) ? petOwner.isEnabled() : petSitter.isEnabled();

            if (accountEnabled) {
                Long userId = (petOwner != null) ? petOwner.getId() : petSitter.getId();
                String userRole = (petOwner != null) ? petOwner.getRole() : petSitter.getRole();

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

    /**
     * Регистрирует нового владельца питомцев.
     *
     * @param user Новый пользователь
     */
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

    /**
     * Регистрирует нового пет-ситтера.
     *
     * @param user Новый пользователь
     */
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
