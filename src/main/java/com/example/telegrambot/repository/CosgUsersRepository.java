package com.example.telegrambot.repository;

import com.example.telegrambot.model.CosgUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CosgUsersRepository extends JpaRepository<CosgUser, Long> {
}
