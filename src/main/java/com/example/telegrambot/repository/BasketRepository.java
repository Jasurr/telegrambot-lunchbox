package com.example.telegrambot.repository;

import com.example.telegrambot.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

    @Query(value = "select * from cosg_basket t where t.chat_id = ?1", nativeQuery = true)
    List<Basket> findByChatId(Long chatId);

    //
    @Modifying
    @Transactional
    @Query(value = "delete from cosg_basket where chat_id = ?1", nativeQuery = true)
    void deleteByChatId(Long chatId);

    @Query(value = "select * from cosg_basket t where t.chat_id = ?1 and t.menu_id = ?2", nativeQuery = true)
    Basket getOneBasket(Long chatId, Long menuId);

    @Query(value = "select t.quantity from cosg_basket t where t.status = 'ACTIVE' and t.menu_id = ?1", nativeQuery = true)
    Integer getAllCount(Long menuId);

}
