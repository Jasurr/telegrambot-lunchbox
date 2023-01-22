package com.example.telegrambot.service;


import com.example.telegrambot.config.BotConfig;
import com.example.telegrambot.model.*;
import com.example.telegrambot.repository.CosgUsersRepository;
import com.example.telegrambot.service.basket.BasketService;
import com.example.telegrambot.service.basket.ExecuteBasketMessage;
import com.example.telegrambot.service.main.ExecuteMainMenu;
import com.example.telegrambot.service.main.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.time.LocalDate;

import static com.example.telegrambot.command.ButtonCommands.TO_BACK;
import static com.example.telegrambot.command.ButtonCommands.TO_BASKET;
import static com.example.telegrambot.command.MainCommandMenu.MENU;
import static com.example.telegrambot.command.MainCommandMenu.MY_ORDERS;
import static com.example.telegrambot.service.basket.BasketButtonConstants.*;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private CosgUsersRepository cosgUsersRepository;
    @Autowired
    private MenuService menuService;
    @Autowired
    private BasketService basketService;
    @Autowired
    private OrderService orderService;

    final String weekDay = LocalDate.now().getDayOfWeek().name();
    final BotConfig config;


    public TelegramBot(BotConfig config) {
        this.config = config;

    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()
                && !update.hasCallbackQuery()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
//            log.info("started: telegram bot");
            System.out.println(messageText);
            switch (messageText) {
                case "/start":
                    registerUser(update.getMessage());
                    prepareAndSendMessage(chatId, "MAIN_MENU");
                    basketService.clearBasket(chatId);
                    break;
                case "/help":
                    prepareAndSendMessage(chatId, HELP_TEXT);
                    break;
                case MENU:
                    showAllMenus(chatId);
                    break;
                case MY_ORDERS:
                    showMyOrders(chatId);
                    break;
                case "/myOrders":
                    break;
                case "/settings":
                    break;
                case "/description":
                    break;
                case "1-taom":
                case "2-taom":
                case "3-taom":
                    choosedMeat(chatId, messageText);
                    break;
                case TO_BASKET:
                    showBasket(chatId, messageText);
                    break;
                case TO_BACK:
                    prepareAndSendMessage(chatId, "MAIN_MENU");
                    break;
                default:
                    prepareAndSendMessage(chatId, "Sorry, command was not recognized");
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            Long menuId = 0l;
            Integer quantity = 0;
            if (callbackData.contains(MINUS_BUTTON) || callbackData.contains(PLUS_BUTTON)) {
                if (callbackData.contains(MINUS_BUTTON)) {
                    menuId = Long.parseLong(callbackData.substring(MINUS_BUTTON.length() + 1));
                    quantity = -1;
                } else if (callbackData.contains(PLUS_BUTTON)) {
                    menuId = Long.parseLong(callbackData.substring(PLUS_BUTTON.length() + 1));
                    quantity = 1;
                }
                boolean b = basketService.updateQuantity(chatId, menuId, quantity);
                if (b)
                    executeEditMessageText(chatId, messageId, menuId);

            } else if (callbackData.contains(ADD_TO_BASKET_BUTTON)) {
                menuId = Long.parseLong(callbackData.substring(ADD_TO_BASKET_BUTTON.length() + 1));
                basketService.updateStatus(chatId, menuId, Status.ACTIVE.name());
                showAllMenus(chatId);
            } else if (callbackData.contains(BACK_BUTTON_IN_BASKET)) {
                System.out.println(BACK_BUTTON_IN_BASKET);
                showAllMenus(chatId);
            } else if (callbackData.contains(APPROVE_ORDER)) {
                createOrder(chatId);
            } else if (callbackData.contains(CLEAR_BASKET)) {
                String msg = "";
                if (!basketService.getCashChatId(chatId).isEmpty()) {
                    basketService.clearBasket(chatId);
                    msg = "Savatcha tozalandi!";
                } else {
                    msg = "Savatchadan mahsulotlar yo'q";
                }
                prepareAndSendMessage(chatId, msg);
            }

        }

    }

    /**
     * Buyurtmalar tarixini ko'rish
     * @param chatId
     */
    private void showMyOrders(Long chatId) {
        executeMessage(new ExecuteMainMenu(orderService, menuService).showMyOrders(chatId));
    }

    /**
     * Korzinkada qancha mahsulotni tanlagan bo'lsa barchasini ko'rsatib beramiz
     *
     * @param chatId
     */
    private void showBasket(Long chatId, String messageText) {
        executeMessage(new ExecuteBasketMessage(menuService, basketService).showBasket(chatId));
    }

    /**
     * Tanlangan taomlarni sonini belgilash va savatchaga qo'shish
     *
     * @param chatId
     * @param messageId
     * @param menuId
     */
    private void executeEditMessageText(long chatId, long messageId, Long menuId) {
        try {
            execute(new ExecuteBasketMessage(menuService, basketService).executeEditMessageText(chatId, messageId, menuId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ovqalarni menusini ko'rsatadi hafta kunlari bo'yicha
     *
     * @param chatId
     */
    private void showAllMenus(long chatId) {
        executeMessage(new ExecuteBasketMessage(menuService, basketService).executeMessage(chatId, weekDay));
    }

    private void registerUser(Message msg) {
        if (cosgUsersRepository.findById(msg.getChatId()).isEmpty()) {
            var chatId = msg.getChatId();
            var chat = msg.getChat();

            CosgUser user = new CosgUser();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            cosgUsersRepository.save(user);
//            log.info("User saved: " + user);
        }
    }

    /**
     * Inline buttonlar ovqatni sonini tanlaydi
     *
     * @param chatId
     * @param messageText
     */
    private void choosedMeat(long chatId, String messageText) {
        executeMessage(new ExecuteBasketMessage(menuService, basketService).choosedMeat(chatId, messageText, weekDay));
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
//            log.error(ERROR_TEXT + e.getMessage());
        }
    }

    /**
     * Main menuni ishga tushirish uchun ishlatiladi
     *
     * @param chatId
     * @param textToSend
     */
    private void prepareAndSendMessage(long chatId, String textToSend) {
        executeMessage(new ExecuteMainMenu(orderService, menuService).prepareAndSendMessage(chatId, textToSend));
    }

    /**
     * Savatchadagi mahsulotlarga buyurtma berish oxirgi jarayon
     *
     * @param chatId
     */
    private void createOrder(Long chatId) {
        if (!basketService.getCashChatId(chatId).isEmpty()) {
            executeMessage(new ExecuteBasketMessage(menuService, basketService).createOrder(chatId, orderService));
            prepareAndSendMessage(chatId, "MAIN_MENU");
        } else {
            prepareAndSendMessage(chatId, "Eng kam buyurtma soni 1. Iltimos oldin mahsulotlarni tanlang!");
            showAllMenus(chatId);
        }
    }
}
