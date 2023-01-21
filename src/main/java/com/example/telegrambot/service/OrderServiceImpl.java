package com.example.telegrambot.service;

import com.example.telegrambot.model.FoodLine;
import com.example.telegrambot.model.Orders;
import com.example.telegrambot.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.example.telegrambot.model.FoodLine.*;

public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    public void createOrder(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));

        message.setText("Ovqat zakaz qiling");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var firstButton = new InlineKeyboardButton();

        firstButton.setText("1 - ovqat");
        firstButton.setCallbackData(FIRST.getName());

        rowInLine.add(firstButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        executeMessage(message);

    }

    private void executeMessage(SendMessage message) {

    }

    @Override
    public void updateOrder(Long chatId, Long orderId) {

    }

    @Override
    public List<Orders> getAll() {
        return null;
    }

    @Override
    public Orders getLastOrder() {
        return null;
    }

    @Override
    public Orders getOne(Long orderId) {
        return null;
    }
}
