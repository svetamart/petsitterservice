package com.example.petsitterservice.service;

import com.example.petsitterservice.model.*;
import com.example.petsitterservice.model.dto.SitterPageReview;
import com.example.petsitterservice.model.dto.SuitableSitterDto;
import com.example.petsitterservice.model.repository.PetBoardingRequestRepository;
import com.example.petsitterservice.model.repository.PetSitterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetSitterService {

    private final PetSitterRepository petSitterRepository;
    private final PetBoardingRequestRepository requestRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(PetSitterService.class);

    @Autowired
    public PetSitterService(PetSitterRepository petSitterRepository, PetBoardingRequestRepository requestRepository,
                            BCryptPasswordEncoder passwordEncoder) {
        this.petSitterRepository = petSitterRepository;
        this.requestRepository = requestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<PetSitter> getAllPetSitters() {
        return petSitterRepository.findAll();
    }

    public void saveSitter(PetSitter sitter) {
        petSitterRepository.save(sitter);
    }

    public PetSitter getById(Long id) {
        return petSitterRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        petSitterRepository.deleteById(id);
    }

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

    public void declineRequest(Long requestId) {
        PetBoardingRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(RequestStatus.DECLINED);
        requestRepository.save(request);
    }

    public void updateTakingNewOrders(Long id, boolean newOrders) {
        PetSitter user = getById(id);
        if (user != null) {
            user.setTakingNewOrders(newOrders);
            petSitterRepository.save(user);
        }
    }

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

    public String[] parseDateRange(String dateRangeString) {
        return dateRangeString.split(" to ");
    }

    public List<String> calculateRequestDates(String start, String end) {
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

    private int convertExperienceStringToYears(String experience) {
        return switch (experience.toLowerCase()) {
            case "меньше года" -> 0;
            case "больше 1 года" -> 1;
            case "больше 3 лет" -> 4;
            case "больше 6 лет" -> 7;
            default -> -1;
        };
    }

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

    private boolean isDogSizeAccepted(PetSitter petSitter, Pet pet) {
        if (pet != null && pet.getSpecies().equalsIgnoreCase("собака")) {
            String dogSize = pet.getSize();
            return isSizeAccepted(petSitter, dogSize);
        }
        return true;
    }

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

    private boolean areOtherPetsAccepted(PetSitter petSitter, PetBoardingRequest request) {
        if (request.isOtherPetsAccepted()) {
            return true;
        } else {
            // Если хозяин не хочет других питомцев, проверяем, есть ли у ситтера другие питомцы
            return !petSitter.hasOwnPets();
        }
    }

    public PetSitter getUserByName(String username) {
        return petSitterRepository.findByUsername(username);
    }

    public void register(PetSitter user) {
        if (petSitterRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username is already taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        petSitterRepository.save(user);
    }

    public void activateAccount(Long userId) {
        PetSitter petSitter = getById(userId);
        if (petSitter != null) {
            petSitter.setAccountEnabled(true);
            petSitterRepository.save(petSitter);
        } else {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }
    }

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

