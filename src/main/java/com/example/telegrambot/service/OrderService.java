package com.example.telegrambot.service;

import com.example.telegrambot.model.Orders;

import java.util.List;

public interface OrderService {

    void createOrder(Orders order);

    List<Orders> getOrdersListByChatId(Long chatId);

    void saveOrders(List<Orders> ordersList);

    void updateOrder(Long chatId, Long orderId);

    List<Orders> getAll();

    Orders getLastOrder();

    Orders getOne(Long orderId);
}
