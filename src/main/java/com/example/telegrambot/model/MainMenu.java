package com.example.telegrambot.model;

public enum MainMenu {
    MENU("Menu"),
    MY_ORDERS("Mening buyurmalarim"),
    DESCRIPTION("O'z firkringizni qoldiring!"),
    SETTINGS("Sozlamalar");
    private String name;

    MainMenu(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
