package com.example.demo.dto;
import java.time.LocalDate;

import com.example.demo.enums.AnimalType;

public class AnimalsRequestDTO {
    // Common fields for all animal types
    private String name;

    private LocalDate birthDate;

    private Double weightKg;

    private String color;
    private String notes;
    private String diet;

    private String gender;

    private boolean neutered;
    private LocalDate lastDeworming;

    
    private AnimalType animalType;

    // Dog specific fields
    private String breed;
    private String size;
    private String coatType;
    private boolean pedigree;

    // Cat specific fields
    private boolean indoorOnly;

    // Bird specific fields
    private String species;
    private boolean clippedWings;
    private boolean talkingAbility;
    // Reptile specific fields
    private String habitatType;
    private String temperatureRequirements;
    private boolean venomous;

    // Fish specific fields
    private String waterType;
    private Double waterTemperature;
    private Double phLevel;
    private String socialBehavior;

    // Rodent specific fields
    private boolean cageTrained;
    private Integer lifespanYears;
    private String teethCondition;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isNeutered() {
        return neutered;
    }

    public void setNeutered(boolean neutered) {
        this.neutered = neutered;
    }

    public LocalDate getLastDeworming() {
        return lastDeworming;
    }

    public void setLastDeworming(LocalDate lastDeworming) {
        this.lastDeworming = lastDeworming;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCoatType() {
        return coatType;
    }

    public void setCoatType(String coatType) {
        this.coatType = coatType;
    }

    public boolean isPedigree() {
        return pedigree;
    }

    public void setPedigree(boolean pedigree) {
        this.pedigree = pedigree;
    }

    public boolean isIndoorOnly() {
        return indoorOnly;
    }

    public void setIndoorOnly(boolean indoorOnly) {
        this.indoorOnly = indoorOnly;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public boolean isClippedWings() {
        return clippedWings;
    }

    public void setClippedWings(boolean clippedWings) {
        this.clippedWings = clippedWings;
    }

    public boolean isTalkingAbility() {
        return talkingAbility;
    }

    public void setTalkingAbility(boolean talkingAbility) {
        this.talkingAbility = talkingAbility;
    }

    public String getHabitatType() {
        return habitatType;
    }

    public void setHabitatType(String habitatType) {
        this.habitatType = habitatType;
    }

    public String getTemperatureRequirements() {
        return temperatureRequirements;
    }

    public void setTemperatureRequirements(String temperatureRequirements) {
        this.temperatureRequirements = temperatureRequirements;
    }

    public boolean isVenomous() {
        return venomous;
    }

    public void setVenomous(boolean venomous) {
        this.venomous = venomous;
    }

    public String getWaterType() {
        return waterType;
    }

    public void setWaterType(String waterType) {
        this.waterType = waterType;
    }

    public Double getWaterTemperature() {
        return waterTemperature;
    }

    public void setWaterTemperature(Double waterTemperature) {
        this.waterTemperature = waterTemperature;
    }

    public Double getPhLevel() {
        return phLevel;
    }

    public void setPhLevel(Double phLevel) {
        this.phLevel = phLevel;
    }

    public String getSocialBehavior() {
        return socialBehavior;
    }

    public void setSocialBehavior(String socialBehavior) {
        this.socialBehavior = socialBehavior;
    }

    public boolean isCageTrained() {
        return cageTrained;
    }

    public void setCageTrained(boolean cageTrained) {
        this.cageTrained = cageTrained;
    }

    public Integer getLifespanYears() {
        return lifespanYears;
    }

    public void setLifespanYears(Integer lifespanYears) {
        this.lifespanYears = lifespanYears;
    }

    public String getTeethCondition() {
        return teethCondition;
    }

    public void setTeethCondition(String teethCondition) {
        this.teethCondition = teethCondition;
    }
}
