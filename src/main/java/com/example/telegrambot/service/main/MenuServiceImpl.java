package com.example.telegrambot.service.main;

import com.example.telegrambot.model.LunchBoxMenu;
import com.example.telegrambot.model.WeekDays;
import com.example.telegrambot.repository.LunchBoxMenuRepository;
import com.example.telegrambot.service.main.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private LunchBoxMenuRepository menuRepository;

    @Override
    public List<LunchBoxMenu> getMenuList() {
        return menuRepository.findAll();
    }

    @Override
    public List<LunchBoxMenu> getMenuListByWeekDay(WeekDays weekDay) {
        return menuRepository.findByWeekday(weekDay);
    }

    @Override
    public LunchBoxMenu getOne(String name) {
        return menuRepository.getByName(name);
    }

    @Override
    public LunchBoxMenu getByLineAndWeekday(Integer line, String weekDay) {
        return menuRepository.getByLineAndWeekday(line, weekDay);
    }

    @Override
    public LunchBoxMenu getMenuById(Long menuId) {
        return menuRepository.findById(menuId).get();
    }

    @Override
    public LunchBoxMenu saveMenu(LunchBoxMenu menu) {
        return menuRepository.save(menu);
    }
}
