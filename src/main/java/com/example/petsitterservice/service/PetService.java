package com.example.petsitterservice.service;


import com.example.petsitterservice.model.Pet;
import com.example.petsitterservice.model.PetOwner;
import com.example.petsitterservice.repository.PetRepository;
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

    public void addPet(Pet pet, PetOwner owner) {
        pet.setOwner(owner);
        owner.addPet(pet);
        petRepository.save(pet);
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
