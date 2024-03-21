package com.example.petsitterservice.service;

import com.example.petsitterservice.model.PersonalRequest;
import com.example.petsitterservice.model.PetOwner;
import com.example.petsitterservice.model.repository.PetOwnerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Сервис для управления владельцами питомцев.
 */
@Service
public class PetOwnerService {
    private final PetOwnerRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Конструктор для создания экземпляра сервиса.
     *
     * @param userRepository   Репозиторий владельцев питомцев
     * @param passwordEncoder       Кодировщик паролей
     */
    @Autowired
    public PetOwnerService(PetOwnerRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Добавляет владельца питомцев в базу.
     *
     * @param user Владелец питомцев
     */
    public void addUser(PetOwner user) {
        userRepository.save(user);
    }

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя
     * @return Владелец питомцев с указанным идентификатором или null, если не найден
     */
    public PetOwner getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Получает список всех владельцев питомцев.
     *
     * @return Список всех владельцев питомцев
     */
    public List<PetOwner> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Получает пользователя по имени.
     *
     * @param username Имя пользователя
     * @return Владелец питомцев, соответствующий имени пользователя, или null, если пользователь не найден
     */
    public PetOwner getUserByName(String username) {
        return userRepository.findByUsername(username);
    }


    /**
     * Регистрирует нового владельца питомцев.
     *
     * @param user Новый пользователь
     * @throws IllegalArgumentException Если имя пользователя уже занято
     */
    public void register(PetOwner user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username is already taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя для удаления
     */
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }


    /**
     * Добавляет персональный запрос в список пользователя.
     *
     * @param owner Владелец питомцев
     * @param request Персональный запрос
     */
    public void addPersonalRequest(PetOwner owner, PersonalRequest request) {
        owner.addPersonalRequest(request);
        userRepository.save(owner);

    }

    /**
     * Активирует аккаунт пользователя.
     *
     * @param userId Имя пользователя
     * @throws EntityNotFoundException Если пользователь с таким id не найден
     */
    public void activateAccount(Long userId) {
        PetOwner petOwner = getUserById(userId);
        if (petOwner != null) {
            petOwner.setAccountEnabled(true);
            userRepository.save(petOwner);
        } else {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }
    }

    /**
     * Деактивирует аккаунт пользователя.
     *
     * @param userId Имя пользователя
     * @throws EntityNotFoundException Если пользователь с таким id не найден
     */
    public void deactivateAccount(Long userId) {
        PetOwner petOwner = getUserById(userId);
        if (petOwner != null) {
            petOwner.setAccountEnabled(false);
            userRepository.save(petOwner);
        } else {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }
    }
}
