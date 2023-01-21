package com.example.telegrambot.repository;

import com.example.telegrambot.model.LunchBoxMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LunchBoxMenuRepository extends JpaRepository<LunchBoxMenu, Long> {
}
