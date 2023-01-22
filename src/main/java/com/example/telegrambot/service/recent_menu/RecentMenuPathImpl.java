package com.example.telegrambot.service.recent_menu;

import com.example.telegrambot.model.RecentMenuPath;
import com.example.telegrambot.repository.RecentMenuPathRepository;
import org.springframework.stereotype.Service;

@Service
public class RecentMenuPathImpl implements RecentMenuPathService {
    private final RecentMenuPathRepository recentMenuPathRepository;

    public RecentMenuPathImpl(RecentMenuPathRepository recentMenuPathRepository) {
        this.recentMenuPathRepository = recentMenuPathRepository;
    }

    @Override
    public RecentMenuPath save(RecentMenuPath menuPath) {
        return recentMenuPathRepository.save(menuPath);
    }

    @Override
    public RecentMenuPath getRecentMenuPath(String recentMenu) {
        return recentMenuPathRepository.getLastMenuPath(recentMenu);
    }
}
