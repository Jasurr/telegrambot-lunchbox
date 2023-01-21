package com.example.telegrambot.model;

public enum FoodLine {
    FIRST("1"),
    SECOND("2"),
    THIRD("3");

    private final String name;

    FoodLine(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
