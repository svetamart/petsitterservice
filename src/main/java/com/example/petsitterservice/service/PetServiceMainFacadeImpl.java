package com.example.petsitterservice.service;

import com.example.petsitterservice.model.*;
import com.example.petsitterservice.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PetServiceMainFacadeImpl - реализация интерфейса PetServiceMainFacade.
 * Этот класс является фасадом для работы с основными операциями приложения,
 * связанными с питомцами, пользователями (владельцами и пет-ситтерами),
 * запросами на передержку и отзывами.
 */

@Service
public class PetServiceMainFacadeImpl implements PetServiceMainFacade{

    private static final String OWNER = "ROLE_OWNER";
    private static final String SITTER = "ROLE_SITTER";
    private static final String ROLE_INVALID = "Invalid user role: ";


    private final PetService petService;
    private final PetOwnerService userService;
    private final PetBoardingRequestService boardingRequestService;
    private final PetSitterService petSitterService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final PersonalRequestService personalRequestService;

    private final ReviewService reviewService;

    /**
     * Конструктор класса.
     *
     * @param petService              Сервис для работы с питомцами
     * @param userService             Сервис для работы с владельцами питомцев
     * @param boardingRequestService  Сервис для работы с запросами на передержку питомцев
     * @param petSitterService        Сервис для работы с пет-ситтерами
     * @param passwordEncoder         Кодировщик паролей
     * @param personalRequestService  Сервис для работы с персональными запросами
     * @param reviewService           Сервис для работы с отзывами
     */
    @Autowired
    public PetServiceMainFacadeImpl(PetService petService, PetOwnerService userService,
                                    PetBoardingRequestService boardingRequestService, PetSitterService petSitterService,
                                    BCryptPasswordEncoder passwordEncoder,
                                    PersonalRequestService personalRequestService,
                                    ReviewService reviewService) {
        this.petService = petService;
        this.userService = userService;
        this.boardingRequestService = boardingRequestService;
        this.petSitterService = petSitterService;
        this.passwordEncoder = passwordEncoder;
        this.personalRequestService = personalRequestService;
        this.reviewService = reviewService;
    }

    /**
     * Добавляет питомца в список владельца.
     *
     * @param pet Питомец
     * @param owner Владелец
     */
    @Override
    public void addPet(PetDto pet, PetOwner owner) {
        petService.addPet(pet, owner);
    }

    /**
     * Получает питомца по его идентификатору.
     *
     * @param petId Идентификатор питомца
     * @return Результат работы метода из petService: Питомец с указанным идентификатором или null, если питомец не найден
     */
    @Override
    public Pet getPetById(Long petId) {
        return petService.getPetById(petId);
    }

    /**
     * Удаляет питомца по его идентификатору.
     *
     * @param petId Идентификатор питомца
     */
    @Override
    public void deletePetById(Long petId) {
        petService.deletePetById(petId);
    }

    /**
     * Получает список всех питомцев.
     *
     * @return Список всех питомцев
     */
    @Override
    public List<Pet> getAllPets() {
        return petService.getAllPets();
    }

    /**
     * Получает список питомцев, принадлежащих указанному владельцу.
     *
     * @param owner Владелец питомцев
     * @return Список питомцев, принадлежащих указанному владельцу
     */
    @Override
    public List<Pet> getPetsByOwner(PetOwner owner) {
        return petService.getPetsByOwner(owner);
    }


    /**
     * Добавляет нового пользователя (владельца питомцев).
     *
     * @param user Новый пользователь
     */
    @Override
    public void addUser(PetOwner user) {
        userService.addUser(user);
    }

    /**
     * Получает пользователя (владельца питомцев) по его идентификатору.
     *
     * @param id Идентификатор пользователя
     * @return Результат работы метода из userService: Пользователь с указанным идентификатором или null, если пользователь не найден
     */
    @Override
    public PetOwner getUserById(Long id) {
        return userService.getUserById(id);
    }

    /**
     * Удаляет пользователя (владельца питомцев) по его идентификатору.
     *
     * @param id Идентификатор пользователя
     */
    @Override
    public void deleteUser(Long id) {
        userService.deleteById(id);
    }

    /**
     * Получает список всех владельцев питомцев.
     *
     * @return Список всех владельцев питомцев
     */
    @Override
    public List<PetOwner> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Создает запрос на передержку питомца.
     *
     * @param user    Пользователь, создающий запрос
     * @param pet     Питомец, для которого создается запрос
     * @param request Данные запроса
     * @return Созданный запрос на передержку питомца
     */
    @Override
    public PetBoardingRequest createRequest(PetOwner user, Pet pet, PetBoardingRequestDto request) {
        return boardingRequestService.createRequest(user, pet, request);
    }

    /**
     * Находит запрос на передержку питомца по его идентификатору.
     *
     * @param requestId Идентификатор запроса на передержку питомца
     * @return Результат работы метода из сервиса boardingRequestService: Запрос на передержку питомца с указанным идентификатором или null, если запрос не найден
     */
    @Override
    public PetBoardingRequest findRequestById(Long requestId) {
        return boardingRequestService.findById(requestId);
    }


    /**
     * Добавляет нового пет-ситтера.
     *
     * @param sitter Новый пет-ситтер
     */
    @Override
    public void addPetSitter(PetSitter sitter) {
        petSitterService.saveSitter(sitter);
    }


    /**
     * Получает список всех пет-ситтеров.
     *
     * @return Список всех пет-ситтеров
     */
    @Override
    public List<PetSitter> getAllPetSitters() {
        return petSitterService.getAllPetSitters();
    }


    /**
     * Добавляет новые даты в календарь пет-ситтера.
     *
     * @param petSitter Пет-ситтер
     * @param availableDates Строка с датами для добавления в календарь
     */
    @Override
    public void addAvailabilityDates(PetSitter petSitter, String availableDates) {
        petSitterService.addAvailabilityDates(petSitter, availableDates);
    }

    /**
     * Находит подходящих пет-ситтеров для запроса на передержку питомца.
     *
     * @param request Запрос на передержку питомца
     * @return Список объектов SuitableSitterDto, представляющих подходящих пет-ситтеров в удобном формате для вывода на страницу
     */
    @Override
    public List<SuitableSitterDto> findSuitableSitters(PetBoardingRequest request) {
        return petSitterService.findSuitableSitters(request);
    }

    /**
     * Находит пет-ситтера по его идентификатору.
     *
     * @param id Идентификатор пет-ситтера
     * @return Результат работы метода из petSitterService: пет-ситтер с указанным идентификатором или null, если не найден
     */
    @Override
    public PetSitter findSitterById(Long id) {
        return petSitterService.getById(id);
    }

    /**
     * Находит все запросы на передержку пет-ситтера.
     *
     * @param sitterId Идентификатор пет-ситтера
     * @return Список запросов на передержку пет-ситтера или empty List, если список пуст
     */
    @Override
    public List<PetBoardingRequest> getSitterRequests(Long sitterId) {
        PetSitter sitter = petSitterService.getById(sitterId);
        if (sitter != null) {
            return sitter.getRequests();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Находит все запросы на передержку владельца питомцев.
     *
     * @param userId Идентификатор владельца питомцев
     * @return Список запросов на передержку или empty List, если список пуст
     */
    @Override
    public List<PetBoardingRequest> getUserRequests(Long userId) {
        PetOwner user = userService.getUserById(userId);
        if (user != null) {
            return user.getRequests();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Обновляет статус запроса на передержку.
     *
     * @param id Идентификатор запроса
     * @param status Новый статус запроса
     */
    @Override
    public void updateRequestStatus(Long id, RequestStatus status) {
        boardingRequestService.updateRequestStatus(id, status);
    }

    /**
     * Принимает запрос на передержку питомца.
     *
     * @param requestId Идентификатор запроса
     */
    @Override
    public void acceptRequest(Long requestId) {
        petSitterService.acceptRequest(requestId);
    }


    /**
     * Отклоняет запрос на передержку питомца.
     *
     * @param requestId Идентификатор запроса
     */
    @Override
    public void declineRequest(Long requestId) {
        petSitterService.declineRequest(requestId);
    }


    /**
     * Обновляет статус поступления новых заказов для пет-ситтера.
     *
     * @param id        Идентификатор пет-ситтера
     * @param newOrders Новое значение статуса поступления заказов
     */
    @Override
    public void updateTakingNewOrders(Long id, boolean newOrders) {
        petSitterService.updateTakingNewOrders(id, newOrders);
    }

    /**
     * Назначает пет-ситтера на запрос о передержке.
     *
     * @param requestId      Идентификатор запроса на передержку
     * @param sitter         Пет-ситтер
     */
    @Override
    public void assignSitter (Long requestId, PetSitter sitter) {
        boardingRequestService.assignSitter(requestId, sitter);
    }

    /**
     * Получает владельца питомцев по имени.
     *
     * @param username Имя пользователя
     * @return Результат работы метода из userService: владелец питомцев, соответствующий имени пользователя, или null, если пользователь не найден
     */
    @Override
    public PetOwner getUserByUsername(String username) {
        return userService.getUserByName(username);
    }


    /**
     * Получает пет-ситтера по имени.
     *
     * @param username Имя пользователя
     * @return Результат работы метода из petSitterService: владелец питомцев, соответствующий имени пользователя, или null, если пользователь не найден
     */
    @Override
    public PetSitter getSitterByUsername(String username) {
        return petSitterService.getUserByName(username);
    }


    /**
     * Создает персональный запрос.
     *
     * @param requestDto Персональный запрос
     */
    @Override
    public void makePersonalRequest(PersonalRequestDto requestDto) {
        PetOwner owner = userService.getUserById(requestDto.getUserId());
        PetBoardingRequest boardingRequest = boardingRequestService.findById(requestDto.getRequestId());
        PersonalRequest request = new PersonalRequest();
        request.setOwner(owner);
        request.setRequest(boardingRequest);
        request.setPhone(requestDto.getPhone());
        personalRequestService.addRequest(request);
        boardingRequestService.updateRequestStatus(boardingRequest.getId(), RequestStatus.PERSONAL);
        userService.addPersonalRequest(owner, request);
    }

    /**
     * Удаляет пользователя с учетом его роли.
     *
     * @param userId Идентификатор пользователя
     * @param userRole Роль пользователя
     * @throws IllegalArgumentException Если роль неопознана
     */
    @Override
    public void deleteUserByRole(String userRole, Long userId) {
        switch (userRole) {
            case OWNER -> userService.deleteById(userId);
            case SITTER -> petSitterService.deleteById(userId);
            default -> throw new IllegalArgumentException(ROLE_INVALID + userRole);
        }
    }

    /**
     * Активирует аккаунт пользователя с учетом его роли.
     *
     * @param userId Идентификатор пользователя
     * @param userRole Роль пользователя
     * @throws IllegalArgumentException Если роль неопознана
     */
    @Override
    public void activateAccountByRole(String userRole, Long userId) {
        switch (userRole) {
            case OWNER -> userService.activateAccount(userId);
            case SITTER -> petSitterService.activateAccount(userId);
            default -> throw new IllegalArgumentException(ROLE_INVALID + userRole);
        }
    }

    /**
     * Деактивирует аккаунт пользователя с учетом его роли.
     *
     * @param userId Идентификатор пользователя
     * @param userRole Роль пользователя
     * @throws IllegalArgumentException Если роль неопознана
     */
    @Override
    public void deactivateAccountByRole(String userRole, Long userId) {
        switch (userRole) {
            case OWNER -> userService.deactivateAccount(userId);
            case SITTER -> petSitterService.deactivateAccount(userId);
            default -> throw new IllegalArgumentException(ROLE_INVALID + userRole);
        }
    }


    /**
     * Создает личный кабинет владельца питомцев.
     *
     * @param user Владелец питомцев
     * @return Личный кабинет пользователя
     */
    @Override
    public PetOwnerDashboard createOwnerDashboard(PetOwner user) {
        PetOwnerDashboard ownerDashboard = new PetOwnerDashboard();

        ownerDashboard.setId(user.getId());
        ownerDashboard.setUsername(user.getUsername());
        ownerDashboard.setName(user.getName());
        ownerDashboard.setLastName(user.getLastName());
        ownerDashboard.setCity(user.getCity());
        ownerDashboard.setPets(user.getPets());

        List<PetBoardingRequest> requests = user.getRequests();
        List<OwnerPageBoardingRequest> sitterPageRequests = new ArrayList<>();
        for (PetBoardingRequest boardingRequest : requests) {
            OwnerPageBoardingRequest ownerPageRequest = OwnerPageBoardingRequest.fromPetBoardingRequest(boardingRequest);

            sitterPageRequests.add(ownerPageRequest);
        }
        ownerDashboard.setRequests(sitterPageRequests);

        return ownerDashboard;
    }

    /**
     * Создает личный кабинет пет-ситтера.
     *
     * @param sitter Владелец питомцев
     * @return Личный кабинет пользователя
     */
    @Override
    public PetSitterDashboard createSitterDashboard(PetSitter sitter) {
        PetSitterDashboard sitterDashboard = new PetSitterDashboard();

        sitterDashboard.setId(sitter.getId());
        sitterDashboard.setUsername(sitter.getUsername());
        sitterDashboard.setName(sitter.getName());
        sitterDashboard.setLastName(sitter.getLastName());
        sitterDashboard.setCity(sitter.getCity());
        sitterDashboard.setBio(sitter.getBio());
        sitterDashboard.setHourlyRate(sitter.getHourlyRate());
        sitterDashboard.setDailyRate(sitter.getDailyRate());
        sitterDashboard.setTakingNewOrders(sitter.isTakingNewOrders());
        sitterDashboard.setAvailabilityDates(sitter.getAvailabilityDates());

        List<Review> reviews = sitter.getReviews();
        List<SitterPageReview> sitterPageReviews = new ArrayList<>();
        for (Review review : reviews) {
            SitterPageReview sitterPageReview = SitterPageReview.fromReview(review);
            sitterPageReviews.add(sitterPageReview);

        }
        sitterDashboard.setReviews(sitterPageReviews);

        List<PetBoardingRequest> requests = sitter.getRequests();
        List<SitterPageBoardingRequest> sitterPageRequests = new ArrayList<>();
        for (PetBoardingRequest boardingRequest : requests) {
            SitterPageBoardingRequest sitterPageRequest = SitterPageBoardingRequest.fromPetBoardingRequest(boardingRequest);
            sitterPageRequests.add(sitterPageRequest);
        }
        sitterDashboard.setRequests(sitterPageRequests);
        return sitterDashboard;
    }

    /**
     * Создает отзыв о пет-ситтере.
     *
     * @param review Отзыв
     */
    @Override
    public void createReview(ReviewDto review) {
        PetBoardingRequest request = boardingRequestService.findById(review.getRequestId());
        PetSitter sitter = petSitterService.getById(request.getSitter().getId());

        request.setReviewed(true);
        boardingRequestService.saveRequest(request);

        Review newReview = new Review();
        newReview.setRequest(request);
        newReview.setMessage(review.getMessage());

        String rating = review.getRating();
        newReview.setRating(rating.replaceAll("\\D", ""));

        reviewService.addReview(newReview);
        sitter.addReview(newReview);
        petSitterService.saveSitter(sitter);
    }


    /**
     * Удаляет отзыв о пет-ситтере.
     *
     * @param id Идентификатор отзыва
     */
    @Override
    public void deleteReview(Long id) {
        reviewService.deleteReview(id);
    }


    /**
     * Регистрирует нового владельца питомцев.
     *
     * @param user Новый пользователь
     */
    @Override
    public void registerPetOwner(PetOwner user) {
        userService.register(user);
    }

    /**
     * Регистрирует нового пет-ситтера.
     *
     * @param user Новый пользователь
     */
    @Override
    public void registerPetSitter(PetSitter user) {
        petSitterService.register(user);
    }


    public boolean isPasswordCorrect(String enteredPassword, String actualPassword) {
        return passwordEncoder.matches(enteredPassword, actualPassword);
    }
}
