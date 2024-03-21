package com.example.petsitterservice.model.dto;

import com.example.petsitterservice.model.Pet;

public class PetForSitterPageRequest {

    private static final String BOY = "мальчик";
    private static final String GIRL = "девочка";
    private static final String SMALL = "маленький";
    private static final String MEDIUM = "средний";
    private static final String BIG = "крупный";


    private String name;
    private String species;
    private String breed;
    private String size;
    private String age;
    private String gender;
    private boolean sterilized;
    private String sterilizedInfo;
    private String sizeInfo;


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
        if (BOY.equalsIgnoreCase(gender) && "Кошка".equalsIgnoreCase(species)) {
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
        if (GIRL.equalsIgnoreCase(gender) && !sterilized) {
            this.sterilizedInfo = "не стерилизована";
        } else if (BOY.equalsIgnoreCase(gender) && !sterilized) {
            this.sterilizedInfo = "не кастрирован";
        } else if (GIRL.equalsIgnoreCase(gender) && sterilized) {
            this.sterilizedInfo = "стерилизована";
        } else if (BOY.equalsIgnoreCase(gender) && sterilized) {
            this.sterilizedInfo = "кастрирован";
        } else {
            this.sterilizedInfo = "информация о стерилизации/кастрации отсутствует";
        }
    }

    public String getSizeInfo() {
        return sizeInfo;
    }

    public void setSizeInfo(String size, String gender) {
        if (GIRL.equalsIgnoreCase(gender)) {
            if (SMALL.equalsIgnoreCase(size)) {
                this.sizeInfo = "маленькая";
            } else if (MEDIUM.equalsIgnoreCase(size)) {
                this.sizeInfo = "средняя";
            } else if (BIG.equalsIgnoreCase(size)) {
                this.sizeInfo = "крупная";
            }
        } else if (BOY.equalsIgnoreCase(gender)) {
            if (SMALL.equalsIgnoreCase(size)) {
                this.sizeInfo = SMALL;
            } else if (MEDIUM.equalsIgnoreCase(size)) {
                this.sizeInfo = MEDIUM;
            } else if (BIG.equalsIgnoreCase(size)) {
                this.sizeInfo = BIG;
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
