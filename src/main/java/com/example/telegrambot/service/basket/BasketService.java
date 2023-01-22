package com.example.telegrambot.service.basket;

import com.example.telegrambot.model.Basket;

import java.util.List;

public interface BasketService {

    Basket saveBasket(Basket basket);

    List<Basket> getCashChatId(Long chatId);

    void clearBasket(Long chatId);

    boolean updateQuantity(Long chatId, Long menuId, Integer quantity);

    Basket getByChatIdAndMenuId(Long chatId, Long menuId);

    boolean updateStatus(Long chatId, Long menuId, String status);
}
