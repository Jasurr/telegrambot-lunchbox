package com.example.telegrambot.service.cash;

import com.example.telegrambot.model.Cash;

import java.util.List;

public interface CashService {

    Cash saveCash(Cash cash);

    List<Cash> getCashChatId(Long chatId);

    void clearCash(Long chatId);

    boolean updateQuantity(Long chatId, Long menuId, Integer quantity);

    Cash getByChatIdAndMenuId(Long chatId, Long menuId);

    boolean updateStatus(Long chatId, Long menuId, String status);
}
