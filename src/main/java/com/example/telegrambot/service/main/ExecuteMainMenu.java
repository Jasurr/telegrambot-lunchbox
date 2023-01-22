package com.example.telegrambot.service.main;

import com.example.telegrambot.model.MainMenu;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ExecuteMainMenu {


    public SendMessage prepareAndSendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        row.add(MainMenu.MENU.getName());

        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add(MainMenu.MY_ORDERS.getName());
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add(MainMenu.DESCRIPTION.getName());
        row.add(MainMenu.SETTINGS.getName());

        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

}
