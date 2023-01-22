package com.example.telegrambot.repository;

import com.example.telegrambot.model.RecentMenuPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecentMenuPathRepository extends JpaRepository<RecentMenuPath, String> {

    @Query(value = "select * from cosg_recent_menu_path where recent_menu = ?1", nativeQuery = true)
    RecentMenuPath getLastMenuPath(String lastMenu);
}
