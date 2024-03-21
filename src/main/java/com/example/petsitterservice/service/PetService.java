package com.example.petsitterservice.service;


import com.example.petsitterservice.model.Pet;
import com.example.petsitterservice.model.PetOwner;
import com.example.petsitterservice.model.dto.PetDto;
import com.example.petsitterservice.model.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для управления питомцами.
 */

@Service
public class PetService {

    private final PetRepository petRepository;

    /**
     * Конструктор для создания экземпляра сервиса.
     *
     * @param petRepository   Репозиторий питомцев
     */

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }


    /**
     * Добавляет питомца в список владельца.
     *
     * @param pet Питомец
     * @param owner Владелец
     */
    public void addPet(PetDto pet, PetOwner owner) {
        Pet newPet = new Pet();
        newPet.setName(pet.getName());

        String ageString = formatAge(pet.getAge(), pet.getAgeUnit());
        newPet.setAge(ageString);

        newPet.setSize(pet.getSize());
        newPet.setSpecies(pet.getSpecies());
        newPet.setBreed(pet.getBreed());
        newPet.setGender(pet.getGender());
        newPet.setSterilized(pet.isSterilized());
        newPet.setOwner(owner);
        owner.addPet(newPet);
        petRepository.save(newPet);
    }

    /**
     * Получает питомца по его идентификатору.
     *
     * @param petId Идентификатор питомца
     * @return Питомец с указанным идентификатором или null, если питомец не найден
     */
    public Pet getPetById(Long petId) {
        return petRepository.findById(petId).orElse(null);
    }

    /**
     * Удаляет питомца по его идентификатору.
     *
     * @param petId Идентификатор питомца
     */
    public void deletePetById(Long petId) {
        petRepository.deleteById(petId);
    }

    /**
     * Получает список всех питомцев из репозитория.
     *
     * @return Список всех питомцев
     */
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }


    /**
     * Получает список питомцев, принадлежащих указанному владельцу.
     *
     * @param owner Владелец питомцев
     * @return Список питомцев, принадлежащих указанному владельцу
     */
    public List<Pet> getPetsByOwner(PetOwner owner) {
        return petRepository.findByOwner(owner);
    }

    /**
     * Вспомогательный метод для форматирования вывода возраста питомца.
     *
     * @param age Возраст
     * @param ageUnit Единица возраста (год, месяц)
     * @return Отформатированная строка с возрастом питомца
     */
    public static String formatAge(int age, String ageUnit) {
        if ("years".equals(ageUnit)) {
            return formatYears(age);
        } else if ("months".equals(ageUnit)) {
            return formatMonths(age);
        } else {
            return age + " " + ageUnit;
        }
    }

    /**
     * Вспомогательный метод для форматирования вывода возраста питомца.
     *
     * @param years Сколько питомцу лет
     * @return Отформатированная строка с корректной формой слова "год" в зависимости от возраста
     */
    private static String formatYears(int years) {
        if (years < 0) {
            return "Некорректный возраст";
        }

        if (years >= 11 && years <= 19) {
            return years + " лет";
        }

        int lastDigit = years % 10;
        if (lastDigit == 1) {
            return years + " год";
        } else if (lastDigit >= 2 && lastDigit <= 4) {
            return years + " года";
        } else {
            return years + " лет";
        }
    }

    /**
     * Вспомогательный метод для форматирования вывода ворзаста питомца.
     *
     * @param months Сколько месяцнв лет
     * @return Отформатированная строка с корректной формой слова "месяц" в зависимости от возраста
     */
    private static String formatMonths(int months) {
        if (months < 0 || months >= 12) {
            return "Некорректный возраст";
        }

        if (months >= 2 && months <= 4) {
            return months + " месяца";
        }
        return months + " месяцев";
    }
}
