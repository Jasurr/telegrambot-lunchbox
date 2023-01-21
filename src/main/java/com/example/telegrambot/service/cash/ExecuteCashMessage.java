package com.example.telegrambot.service.cash;

import com.example.telegrambot.model.Cash;
import com.example.telegrambot.model.CashIdentity;
import com.example.telegrambot.model.LunchBoxMenu;
import com.example.telegrambot.model.WeekDays;
import com.example.telegrambot.service.main.MenuService;
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

import static com.example.telegrambot.service.cash.CashButtonConstants.*;

public class ExecuteCashMessage {

    private final MenuService menuService;
    private final CashService cashService;
    private SendMessage message = new SendMessage();


    public ExecuteCashMessage(MenuService menuService, CashService cashService) {
        this.menuService = menuService;
        this.cashService = cashService;
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
        var cash = cashService.getByChatIdAndMenuId(chatId, menuId);
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
        Cash cash = new Cash();
        CashIdentity cashIdentity = new CashIdentity();
        cashIdentity.setMenuId(menu.getMenuId());
        cashIdentity.setChatId(chatId);
        cash.setCashIdentity(cashIdentity);
        cashService.saveCash(cash);
        return message;
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(LunchBoxMenu menu, String text) {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
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
        addToCashButton.setCallbackData(ADD_TO_CASH_BUTTON + "_" + menu.getMenuId());


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
        NumberFormat formatter = new DecimalFormat("#,###,###");
        messageText.append(formatter.format(amount));
        messageText.append(" so'm</b>");
        messageText.append("\n");
        return messageText.toString();
    }
}
