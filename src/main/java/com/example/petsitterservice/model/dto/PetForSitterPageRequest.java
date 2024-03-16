package com.example.petsitterservice.model.dto;

import com.example.petsitterservice.model.Pet;

public class PetForSitterPageRequest {

    private String name;
    private String species;
    private String breed;
    private String size;
    private String age;
    private String gender;
    private boolean sterilized;
    private String sterilizedInfo;
    private String sizeInfo;

    public PetForSitterPageRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species, String gender) {
        if ("мальчик".equalsIgnoreCase(gender) && "Кошка".equalsIgnoreCase(species)) {
            this.species = "кот";
        } else {
            this.species = species.toLowerCase();
        }
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed.toLowerCase();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age.toLowerCase();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {

        this.gender = gender.toLowerCase();
    }

    public boolean isSterilized() {
        return sterilized;
    }

    public void setSterilized(boolean sterilized) {
        this.sterilized = sterilized;
    }

    public String getSterilizedInfo() {
        return sterilizedInfo;
    }

    public void setSterilizedInfo(String gender, boolean sterilized) {
        if ("девочка".equalsIgnoreCase(gender) && !sterilized) {
            this.sterilizedInfo = "не стерилизована";
        } else if ("мальчик".equalsIgnoreCase(gender) && !sterilized) {
            this.sterilizedInfo = "не кастрирован";
        } else if ("девочка".equalsIgnoreCase(gender) && sterilized) {
            this.sterilizedInfo = "стерилизована";
        } else if ("мальчик".equalsIgnoreCase(gender) && sterilized) {
            this.sterilizedInfo = "кастрирован";
        } else {
            this.sterilizedInfo = "информация о стерилизации/кастрации отсутствует";
        }
    }

    public String getSizeInfo() {
        return sizeInfo;
    }

    public void setSizeInfo(String size, String gender) {
        if ("девочка".equalsIgnoreCase(gender)) {
            if ("маленький".equalsIgnoreCase(size)) {
                this.sizeInfo = "маленькая";
            } else if ("средний".equalsIgnoreCase(size)) {
                this.sizeInfo = "средняя";
            } else if ("крупный".equalsIgnoreCase(size)) {
                this.sizeInfo = "крупная";
            }
        } else if ("мальчик".equalsIgnoreCase(gender)) {
            if ("маленький".equalsIgnoreCase(size)) {
                this.sizeInfo = "маленький";
            } else if ("средний".equalsIgnoreCase(size)) {
                this.sizeInfo = "средний";
            } else if ("крупный".equalsIgnoreCase(size)) {
                this.sizeInfo = "крупный";
            }
        }
    }

    public static PetForSitterPageRequest fromPet(Pet pet) {
        PetForSitterPageRequest petRequest = new PetForSitterPageRequest();
        petRequest.setName(pet.getName());
        petRequest.setBreed(pet.getBreed());
        petRequest.setSize(pet.getSize());
        petRequest.setAge(pet.getAge());
        petRequest.setGender(pet.getGender());
        petRequest.setSpecies(pet.getSpecies(), pet.getGender());
        petRequest.setSterilized(pet.isSterilized());
        petRequest.setSterilizedInfo(pet.getGender(), pet.isSterilized());
        petRequest.setSizeInfo(pet.getSize(), pet.getGender());
        return petRequest;
    }
}
