package com.example.telegrambot.service.recent_menu;


import com.example.telegrambot.model.RecentMenuPath;

public interface RecentMenuPathService {

    RecentMenuPath save(RecentMenuPath menuPath);

    RecentMenuPath getRecentMenuPath(String recentMenu);

}
