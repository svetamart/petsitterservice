package com.example.petsitterservice.service;

import com.example.petsitterservice.model.*;
import com.example.petsitterservice.model.dto.SuitableSitterDto;
import com.example.petsitterservice.model.repository.PetBoardingRequestRepository;
import com.example.petsitterservice.model.repository.PetSitterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления пет-ситтерами.
 */
@Service
public class PetSitterService {

    private final PetSitterRepository petSitterRepository;
    private final PetBoardingRequestRepository requestRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Конструктор для создания экземпляра сервиса.
     *
     * @param petSitterRepository   Репозиторий пет-ситтеров
     * @param requestRepository     Репозиторий запросов на передержку
     * @param passwordEncoder       Кодировщик паролей
     */
    @Autowired
    public PetSitterService(PetSitterRepository petSitterRepository, PetBoardingRequestRepository requestRepository,
                            BCryptPasswordEncoder passwordEncoder) {
        this.petSitterRepository = petSitterRepository;
        this.requestRepository = requestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Получает список всех пет-ситтеров.
     *
     * @return Список всех пет-ситтеров
     */
    public List<PetSitter> getAllPetSitters() {
        return petSitterRepository.findAll();
    }

    /**
     * Сохраняет пет-ситтера в базе данных.
     *
     * @param sitter Пет-ситтер для сохранения
     */
    public void saveSitter(PetSitter sitter) {
        petSitterRepository.save(sitter);
    }

    /**
     * Получает пет-ситтера по его идентификатору.
     *
     * @param id Идентификатор пет-ситтера
     * @return Пет-ситтер с указанным идентификатором или null, если не найден
     */
    public PetSitter getById(Long id) {
        return petSitterRepository.findById(id).orElse(null);
    }

    /**
     * Удаляет пет-ситтера по его идентификатору.
     *
     * @param id Идентификатор пет-ситтера для удаления
     */
    public void deleteById(Long id) {
        petSitterRepository.deleteById(id);
    }


    /**
     * Принимает запрос на передержку питомца.
     *
     * @param requestId Идентификатор запроса
     * @throws RuntimeException Если запрос не найден или некоторые даты не доступны
     */
    public void acceptRequest(Long requestId) {
        PetBoardingRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        PetSitter sitter = request.getSitter();
        List<String> requestDates = calculateRequestDates(request.getStartDate(), request.getEndDate());
        List<String> availableDates = sitter.getAvailabilityDates();
        if (availableDates.containsAll(requestDates)) {
            availableDates.removeAll(requestDates);

            request.setStatus(RequestStatus.ACCEPTED);
            requestRepository.save(request);
            petSitterRepository.save(sitter);
        } else {
            throw new RuntimeException("Some of the boarding request dates are not available");
        }
    }

    /**
     * Отклоняет запрос на передержку питомца.
     *
     * @param requestId Идентификатор запроса
     * @throws RuntimeException Если запрос не найден
     */
    public void declineRequest(Long requestId) {
        PetBoardingRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(RequestStatus.DECLINED);
        requestRepository.save(request);
    }

    /**
     * Обновляет статус поступления новых заказов для пет-ситтера.
     *
     * @param id        Идентификатор пет-ситтера
     * @param newOrders Новое значение статуса поступления заказов
     */
    public void updateTakingNewOrders(Long id, boolean newOrders) {
        PetSitter user = getById(id);
        if (user != null) {
            user.setTakingNewOrders(newOrders);
            petSitterRepository.save(user);
        }
    }

    /**
     * Добавляет доступные даты для передержки питомца в календарь пет-ситтера.
     *
     * @param petSitter        Пет-ситтер, для которого добавляются даты
     * @param dateRangeString Строка, представляющая диапазон дат
     */
    public void addAvailabilityDates(PetSitter petSitter, String dateRangeString) {
        List<String> currentAvailability = petSitter.getAvailabilityDates();
        String[] startAndEndDates = parseDateRange(dateRangeString);
        List<String> datesToAdd = calculateRequestDates(startAndEndDates[0], startAndEndDates[1]);

        for (String date : datesToAdd) {
            if (!currentAvailability.contains(date)) {
                currentAvailability.add(date);
            }
        }
        petSitter.setAvailabilityDates(currentAvailability);
        petSitterRepository.save(petSitter);
    }

    /**
     * Разбивает строку диапазона дат на массив из двух строк, представляющих начальную и конечную даты.
     *
     * @param dateRangeString Строка с диапазоном дат
     * @return Массив строк с начальной и конечной датами
     */
    private String[] parseDateRange(String dateRangeString) {
        return dateRangeString.split(" to ");
    }

    /**
     * Вычисляет даты между начальной и конечной датами, включая обе даты.
     *
     * @param start Начальная дата
     * @param end   Конечная дата
     * @return Список строк, представляющих даты между начальной и конечной датами включительно
     */
    private List<String> calculateRequestDates(String start, String end) {
        List<String> dates = new ArrayList<>();

        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate.toString());
            currentDate = currentDate.plusDays(1);
        }

        return dates;
    }

    /**
     * Находит подходящих пет-ситтеров для запроса на передержку питомца.
     *
     * @param request Запрос на передержку питомца
     * @return Список объектов SuitableSitterDto, представляющих подходящих пет-ситтеров в удобном формате для вывода на страницу
     */
    public List<SuitableSitterDto> findSuitableSitters(PetBoardingRequest request) {
        List<PetSitter> allSitters = petSitterRepository.findAll();

        String startDate = request.getStartDate();
        String endDate = request.getEndDate();

        Pet pet = request.getPet();
        PetOwner user = request.getUser();

        List<PetSitter> suitableSitters = allSitters.stream()
                .filter(petSitter -> user.isAccountEnabled())
                .filter(petSitter -> user.getCity().equals(petSitter.getCity()))
                .filter(petSitter -> isDateRangeAvailable(petSitter.getAvailabilityDates(), startDate, endDate))
                .filter(petSitter -> isSpeciesAccepted(petSitter, pet))
                .filter(petSitter -> isDogSizeAccepted(petSitter, pet))
                .filter(petSitter -> areOtherPetsAccepted(petSitter, request))
                .filter(petSitter -> checkExperience(petSitter, request))
                .collect(Collectors.toList());

        if (suitableSitters.isEmpty()) {
            request.setStatus(RequestStatus.UNPROCESSED);
            requestRepository.save(request);
        }

        List<SuitableSitterDto> result = new ArrayList<>();
        for (PetSitter sitter : suitableSitters) {
            SuitableSitterDto resultSitter = SuitableSitterDto.fromSitter(sitter);
            result.add(resultSitter);

        }
        return result;
    }

    /**
     * Проверяет, принимает ли пет-ситтер животное указанного вида.
     *
     * @param petSitter Пет-ситтер
     * @param pet       Животное
     * @return true, если пет-ситтер принимает животное указанного вида, иначе false
     */
    private boolean isSpeciesAccepted(PetSitter petSitter, Pet pet) {
        String species = pet.getSpecies().toLowerCase();
        if (species.length() > 0) {
            species = species.substring(0, species.length() - 1);
        }
        for (String acceptedPet : petSitter.getAcceptedPets()) {
            if (acceptedPet.toLowerCase().startsWith(species)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяет опыт пет-ситтера на соответствие требованиям запроса на передержку.
     *
     * @param petSitter Пет-ситтер
     * @param request   Запрос на передержку
     * @return true, если опыт пет-ситтера удовлетворяет требованиям, иначе false
     */
    private boolean checkExperience(PetSitter petSitter, PetBoardingRequest request) {
        if (request.getSitterExperience().equalsIgnoreCase("неважно")) {
            return true;
        }
        else {
            int requiredExperienceYears = convertExperienceStringToYears(request.getSitterExperience());
            int petSitterExperienceYears = convertExperienceStringToYears(petSitter.getExperience());

            return petSitterExperienceYears >= requiredExperienceYears;
        }
    }

    /**
     * Преобразует строку опыта работы в годах.
     *
     * @param experience Строка, представляющая опыт работы
     * @return Количество лет опыта работы
     */
    private int convertExperienceStringToYears(String experience) {
        return switch (experience.toLowerCase()) {
            case "меньше года" -> 0;
            case "больше 1 года" -> 1;
            case "больше 3 лет" -> 4;
            case "больше 6 лет" -> 7;
            default -> -1;
        };
    }

    /**
     * Проверяет доступность диапазона дат для пет-ситтера.
     *
     * @param availableDates Доступные даты пет-ситтера
     * @param startDate      Начальная дата диапазона
     * @param endDate        Конечная дата диапазона
     * @return true, если диапазон дат доступен для пет-ситтера, иначе false
     */
    private boolean isDateRangeAvailable(List<String> availableDates, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            if (!availableDates.contains(date.toString())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверяет, принимает ли пет-ситтер собак указанного размера.
     *
     * @param petSitter Пет-ситтер
     * @param pet       Животное
     * @return true, если животное не является собакой или пет-ситтер принимает собак указанного размера, иначе false
     */
    private boolean isDogSizeAccepted(PetSitter petSitter, Pet pet) {
        if (pet != null && pet.getSpecies().equalsIgnoreCase("собака")) {
            String dogSize = pet.getSize();
            return isSizeAccepted(petSitter, dogSize);
        }
        return true;
    }

    /**
     * Вспомогательный метод для проверки размера собаки.
     *
     * @param petSitter Пет-ситтер
     * @param dogSize   Размер собаки
     * @return true, если пет-ситтер принимает собак указанного размера, иначе false
     */
    private boolean isSizeAccepted(PetSitter petSitter, String dogSize) {
        if (dogSize.length() > 0) {
            dogSize = dogSize.substring(0, dogSize.length() - 2);
        }
        for (String acceptedSize : petSitter.getAcceptedDogSizes()) {
            if (acceptedSize.toLowerCase().startsWith(dogSize.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяет наличие в запросе ограничений для собственных питомцев пет-ситтера.
     *
     * @param petSitter Пет-ситтер
     * @param request   Запрос на передержку
     * @return true, если владелец не против животных пет-ситтера или если у ситтера нет собственных животных, иначе false
     */

    private boolean areOtherPetsAccepted(PetSitter petSitter, PetBoardingRequest request) {
        if (request.isOtherPetsAccepted()) {
            return true;
        } else {
            return !petSitter.hasOwnPets();
        }
    }

    /**
     * Получает пет-ситтера по имени пользователя.
     *
     * @param username Имя пользователя
     * @return Пет-ситтер, соответствующий имени пользователя, или null, если пет-ситтер не найден
     */
    public PetSitter getUserByName(String username) {
        return petSitterRepository.findByUsername(username);
    }


    /**
     * Регистрирует нового пет-ситтера.
     *
     * @param user Новый пет-ситтер
     * @throws IllegalArgumentException Если имя пользователя уже занято
     */
    public void register(PetSitter user) {
        if (petSitterRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username is already taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        petSitterRepository.save(user);
    }

    /**
     * Активирует аккаунт пользователя.
     *
     * @param userId Имя пользователя
     * @throws EntityNotFoundException Если пользователь с таким id не найден
     */
    public void activateAccount(Long userId) {
        PetSitter petSitter = getById(userId);
        if (petSitter != null) {
            petSitter.setAccountEnabled(true);
            petSitterRepository.save(petSitter);
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
        PetSitter petSitter = getById(userId);
        if (petSitter != null) {
            petSitter.setAccountEnabled(false);
            petSitterRepository.save(petSitter);
        } else {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }
    }
}

