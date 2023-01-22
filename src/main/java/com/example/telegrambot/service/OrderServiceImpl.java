package com.example.telegrambot.service;

import com.example.telegrambot.model.Orders;
import com.example.telegrambot.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    public void createOrder(Orders order) {
        ordersRepository.save(order);
    }

    @Override
    public List<Orders> getOrdersListByChatId(Long chatId) {
        return ordersRepository.getOrdersByChatId(chatId);
    }

    @Override
    public void saveOrders(List<Orders> ordersList) {
        ordersRepository.saveAll(ordersList);
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
