package com.example.telegrambot.repository;

import com.example.telegrambot.model.LunchBoxMenu;
import com.example.telegrambot.model.WeekDays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LunchBoxMenuRepository extends JpaRepository<LunchBoxMenu, Long> {

    List<LunchBoxMenu> findByWeekday(WeekDays weekDays);

    LunchBoxMenu getByName(String name);

    @Query(value = "select * from cosg_lunch_box_menu t where t.line = ?1 and t.weekday = ?2", nativeQuery = true)
    LunchBoxMenu getByLineAndWeekday(Integer line, String weekDay);
}
