package com.example.petsitterservice.service;


import com.example.petsitterservice.model.Pet;
import com.example.petsitterservice.model.PetOwner;
import com.example.petsitterservice.model.dto.PetDto;
import com.example.petsitterservice.model.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public void addPet(PetDto pet, PetOwner owner) {
        Pet newPet = new Pet();
        newPet.setName(pet.getName());

        String ageString = PetServiceMainFacadeImpl.formatAge(pet.getAge(), pet.getAgeUnit());
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

    public Pet getPetById(Long petId) {
        return petRepository.findById(petId).orElse(null);
    }

    public void deletePetById(Long petId) {
        petRepository.deleteById(petId);
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwner(PetOwner owner) {
        return petRepository.findByOwner(owner);
    }
}
