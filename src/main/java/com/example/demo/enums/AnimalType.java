package com.example.demo.enums;

public enum AnimalType {
    
    DOG("Dog"),
    CAT("Cat"),
    BIRD("Bird"),
    REPTILE("Reptile"),
    FISH("Fish"),
    RODENT("Rodent"),
    OTHER("Other");

    private final String displayName;

    AnimalType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
