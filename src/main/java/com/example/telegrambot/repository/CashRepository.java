package com.example.telegrambot.repository;

import com.example.telegrambot.model.Cash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashRepository extends JpaRepository<Cash, Long> {

    @Query(value = "select * from cosg_cash t where t.chat_id = ?1", nativeQuery = true)
    List<Cash> findByChatId(Long chatId);

    //
    @Query(value = "delete cosg_cash t where t.chat_id = ?1", nativeQuery = true)
    void deleteByChatId(Long chatId);

    @Query(value = "select * from cosg_cash t where t.chat_id = ?1 and t.menu_id = ?2", nativeQuery = true)
    Cash getOneCash(Long chatId, Long menuId);

    @Query(value = "select t.quantity from cosg_cash t where t.status = 'ACTIVE' and t.menu_id = ?1", nativeQuery = true)
    Integer getAllCount(Long menuId);

    @Query(value = "update cosg_cash set status = ?3 where chat_id = ?1 and menu_id = ?2", nativeQuery = true)
    void updateStatus(Long chatId, Long menuId, String status);

}
