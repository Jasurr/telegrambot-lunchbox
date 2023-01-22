package com.example.telegrambot.repository;

import com.example.telegrambot.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {


    List<Orders> getOrdersByChatId(Long chatId);
}
