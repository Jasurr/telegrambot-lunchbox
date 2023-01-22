package com.example.telegrambot.service.basket;

import com.example.telegrambot.model.*;
import com.example.telegrambot.service.OrderService;
import com.example.telegrambot.service.main.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.telegrambot.command.ButtonCommands.TO_BACK;
import static com.example.telegrambot.command.ButtonCommands.TO_BASKET;
import static com.example.telegrambot.service.basket.BasketButtonConstants.*;

public class ExecuteBasketMessage {

    private final MenuService menuService;
    private final BasketService basketService;


    private SendMessage message = new SendMessage();

    private InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
    private List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
    private List<InlineKeyboardButton> rowInLine = new ArrayList<>();


    public ExecuteBasketMessage(MenuService menuService, BasketService basketService) {
        this.menuService = menuService;
        this.basketService = basketService;
    }


    public SendMessage executeMessage(Long chatId, String weekDay) {
        message.setChatId(String.valueOf(chatId));
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        System.out.println(LocalDate.now().getDayOfWeek().name());
        List<LunchBoxMenu> menus = menuService.getMenuListByWeekDay(WeekDays.valueOf(weekDay));

        StringBuilder messageText = new StringBuilder();
        messageText.append("<strong>Bugungi taomlar ro'yxati:</strong>\n");
        messageText.append("-----------------------------------------------------\n");
        messageText.append("Birorta ovqatni tanlang!\n");
        menus.forEach(r -> {
            messageText.append(getMessage(r.getName(), r.getLine(), r.getAmount(), r.getQuantity()));
            row.add(r.getLine() + "-taom");
        });

        message.setText(messageText.toString());
        message.enableHtml(true);
        keyboardRows.add(row);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(TO_BASKET);
        row2.add(TO_BACK);

        keyboardRows.add(row2);

        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        keyboardMarkup.setOneTimeKeyboard(true);
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

    public EditMessageText executeEditMessageText(long chatId,
                                                  long messageId,
                                                  Long menuId) {
        var menu = menuService.getMenuById(menuId);
        var cash = basketService.getByChatIdAndMenuId(chatId, menuId);
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setMessageId((int) messageId);

        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append(getMessage(menu.getName(), menu.getLine(), menu.getAmount(), menu.getQuantity()));

        message.setText(textBuilder.toString());
        InlineKeyboardMarkup markupInLine = getInlineKeyboardMarkup(menu, String.valueOf(cash.getQuantity()));
        message.setReplyMarkup(markupInLine);
        message.enableHtml(true);

        return message;
    }

    public SendMessage choosedMeat(long chatId, String messageText, String weekDay) {
        message = new SendMessage();
        message.setChatId(String.valueOf(chatId));

        Integer line = Integer.parseInt(messageText.substring(0, 1));
        LunchBoxMenu menu = menuService.getByLineAndWeekday(line, weekDay);

        StringBuilder text = new StringBuilder();
        text.append(getMessage(menu.getName(), menu.getLine(), menu.getAmount(), menu.getQuantity()));
        message.setText(text.toString());
        InlineKeyboardMarkup markupInLine = getInlineKeyboardMarkup(menu, "1");
        message.setReplyMarkup(markupInLine);
        message.enableHtml(true);
//      Korzinkaga saqlash
        Basket cash = new Basket();
        BasketIdentity basketIdentity = new BasketIdentity();
        basketIdentity.setMenuId(menu.getMenuId());
        basketIdentity.setChatId(chatId);
        cash.setBasketIdentity(basketIdentity);
        basketService.saveBasket(cash);
        return message;
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(LunchBoxMenu menu, String text) {
        markupInLine = new InlineKeyboardMarkup();
        rowsInLine = new ArrayList<>();
        rowInLine = new ArrayList<>();
        var minusButton = new InlineKeyboardButton();

        minusButton.setText("-");
        minusButton.setCallbackData(MINUS_BUTTON + "_" + menu.getMenuId());

        var quantityButton = new InlineKeyboardButton();

        quantityButton.setText(text);
        quantityButton.setCallbackData(QUANTITY_BUTTON + "_" + menu.getMenuId());

        var plusButton = new InlineKeyboardButton();

        plusButton.setText("+");
        plusButton.setCallbackData(PLUS_BUTTON + "_" + menu.getMenuId());

        var addToCashButton = new InlineKeyboardButton();
        addToCashButton.setText("Savatchaga qo'sish");
        addToCashButton.setCallbackData(ADD_TO_BASKET_BUTTON + "_" + menu.getMenuId());


        rowInLine.add(minusButton);
        rowInLine.add(quantityButton);
        rowInLine.add(plusButton);

        rowsInLine.add(rowInLine);
        rowInLine = new ArrayList<>();
        rowInLine.add(addToCashButton);
        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);


//        markupInLine.setKeyboard(rowsInLine);

        return markupInLine;
    }
    private String getMessage(String name, Integer line, Double amount, Integer quantity) {
        StringBuilder messageText = new StringBuilder();
        messageText.append("<i>");
        messageText.append(line);
        messageText.append("- taom </i>");
        messageText.append(name);
        messageText.append(" - ");
        messageText.append(quantity);
        messageText.append(" ta bor");
        messageText.append(", ");
        messageText.append("narxi: <b>");
        formatAmount(amount, messageText);
        messageText.append(" so'm</b>");
        messageText.append("\n");
        return messageText.toString();
    }

    private void formatAmount(Double amount, StringBuilder messageText) {
        NumberFormat formatter = new DecimalFormat("#,###,###");
        messageText.append(formatter.format(amount));
    }

    public SendMessage showBasket(Long chatId) {
        message.setChatId(String.valueOf(chatId));
        List<Basket> basketList = basketService.getCashChatId(chatId);

        StringBuilder str = new StringBuilder("Savatchada:\n");

        AtomicReference<Double> amount = new AtomicReference<>(0.0);
        basketList.forEach(r -> {
            LunchBoxMenu menu = menuService.getMenuById(r.getBasketIdentity().getMenuId());
            str.append("<i>");
            str.append(r.getQuantity() * r.getQuantity() + " x ");
            str.append(menu.getName());
            str.append("</i> narxi:<b>");
            formatAmount(menu.getAmount(), str);
            str.append("</b>\n");
            amount.set((double) (amount.get() + r.getQuantity() * menu.getAmount()));
        });
        str.append("Mahsulotlar: <b>");
        formatAmount(amount.get(), str);
        str.append("</b>\n");
       /* str.append("Yetkazib berish narxi: ");
        str.append("<b>10,000</b>");
        str.append("\n");*/
        str.append("Jami: <b>");
        formatAmount((amount.get()), str);
        str.append("</b>\n");

        message.setText(str.toString());

        rowsInLine = new ArrayList<>();
        rowInLine = new ArrayList<>();

        var backButton = new InlineKeyboardButton();

        backButton.setText("Orqaga");
        backButton.setCallbackData(BACK_BUTTON_IN_BASKET);

        var approveOrder = new InlineKeyboardButton();

        approveOrder.setText("Buyurtmani tasdiqlash");
        approveOrder.setCallbackData(APPROVE_ORDER);


        var clearBasket = new InlineKeyboardButton();
        clearBasket.setText("Savatchani tozalash");
        clearBasket.setCallbackData(CLEAR_BASKET);


        rowInLine.add(backButton);
        rowInLine.add(approveOrder);

        rowsInLine.add(rowInLine);
        rowInLine = new ArrayList<>();
        rowInLine.add(clearBasket);
        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
        message.enableHtml(true);

        return message;
    }

    public SendMessage createOrder(Long chatId, OrderService orderService) {
        message.setChatId(String.valueOf(chatId));

        List<Basket> basketList = basketService.getCashChatId(chatId);
        List<Orders> ordersList = new ArrayList<>();

        basketList.forEach(r -> {
            Orders order = new Orders();
            LunchBoxMenu menu = menuService.getMenuById(r.getBasketIdentity().getMenuId());
            order.setChatId(chatId);
            order.setMenuId(menu.getMenuId());
            order.setAmount(r.getQuantity() * menu.getAmount());
            ordersList.add(order);
            orderService.createOrder(order);
        });
//        orderService.saveOrders(ordersList);
        basketService.clearBasket(chatId);
        message.setText("Buyurmangiz qabul qilindi.");
        return message;
    }
}
