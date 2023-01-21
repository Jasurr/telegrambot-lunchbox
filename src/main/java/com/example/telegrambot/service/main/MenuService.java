package com.example.telegrambot.service.main;

import com.example.telegrambot.model.LunchBoxMenu;
import com.example.telegrambot.model.WeekDays;

import java.util.List;

public interface MenuService {

    List<LunchBoxMenu> getMenuList();

    List<LunchBoxMenu> getMenuListByWeekDay(WeekDays weekDay);

    LunchBoxMenu getOne(String name);

    LunchBoxMenu getByLineAndWeekday(Integer line, String weekDay);

    LunchBoxMenu getMenuById(Long menuId);

    LunchBoxMenu saveMenu(LunchBoxMenu menu);
}
