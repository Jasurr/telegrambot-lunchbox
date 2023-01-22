package com.example.telegrambot.service.main;

import com.example.telegrambot.model.LunchBoxMenu;
import com.example.telegrambot.model.MainMenu;
import com.example.telegrambot.model.Orders;
import com.example.telegrambot.service.OrderService;
import org.aspectj.apache.bcel.generic.RET;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.telegrambot.model.PaymentStatus.*;

public class ExecuteMainMenu {
    SendMessage message = new SendMessage();

    private final OrderService orderService;
    private final MenuService menuService;

    public ExecuteMainMenu(OrderService orderService, MenuService menuService) {
        this.orderService = orderService;
        this.menuService = menuService;
    }


    public SendMessage prepareAndSendMessage(long chatId, String textToSend) {

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
    private void formatAmount(Double amount, StringBuilder messageText) {
        NumberFormat formatter = new DecimalFormat("#,###,###");
        messageText.append(formatter.format(amount));
    }
    public static final String getDayName(final Date date) {
        SimpleDateFormat df = new SimpleDateFormat("EEEE", new Locale("UZ"));
        return df.format(date);
    }
    public SendMessage showMyOrders(Long chatId) {
        List<Orders> ordersList = orderService.getOrdersListByChatId(chatId);
        StringBuilder str = new StringBuilder();

        message.setChatId(chatId);

        str.append("Barcha haridlar tarixi:\n");
        AtomicReference<Double> notPaidAmount = new AtomicReference<>(0.0);
        AtomicReference<Double> unapprovedAmount = new AtomicReference<>(0.0);
        AtomicReference<Double> paid = new AtomicReference<>(0.0);
        AtomicReference<Double> total = new AtomicReference<>(0.0);

        ordersList.forEach(r -> {
            GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
            cal.setTime(r.getRegisteredAt());
            LunchBoxMenu menu = menuService.getMenuById(r.getMenuId());
            str.append("<i>");
            str.append(getDayName(r.getRegisteredAt()));
            str.append(" ");
            str.append(r.getRegisteredAt());
            str.append("</i>\n<i>");

            str.append(r.getQuantity());
            str.append(" ta - " + menu.getName());
            str.append("</i>");
            str.append(", summa:");
            str.append(r.getAmount());
            str.append(" so'm");
            str.append("holati: ");
            str.append(r.getPaymentStatus().name());
            str.append("\n\n");
            if (r.getPaymentStatus().equals(NOT_PAID_YET))
                notPaidAmount.updateAndGet(v -> v + r.getAmount());
            if (r.getPaymentStatus().equals(UNAPPROVED_PAYMENT))
                unapprovedAmount.updateAndGet(v -> v + r.getAmount());
            if (r.getPaymentStatus().equals(PAID))
                paid.updateAndGet(v -> v + r.getAmount());

            total.updateAndGet(v -> v + r.getAmount());
        });

        str.append("\n");
        str.append("-------------------------------------------\n");
        str.append("To'lanmagan xaridlar: ");
        formatAmount(notPaidAmount.get(), str);
        str.append("\n");
        str.append("To'langan lekin tasdiqlanmaganlari: ");
        formatAmount(unapprovedAmount.get(), str);
        str.append("\n");
        str.append("To'langanlari: ");
        formatAmount(paid.get(), str);
        str.append("\n");
        str.append("Jami: ");
        formatAmount(total.get(), str);
        str.append("\n");
        str.append("-------------------------------------------\n");

        message.setText(str.toString());
        message.enableHtml(true);
        return message;
    }

}
