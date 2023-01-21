package com.example.telegrambot.service.cash;

import com.example.telegrambot.model.Cash;
import com.example.telegrambot.model.LunchBoxMenu;
import com.example.telegrambot.repository.CashRepository;
import com.example.telegrambot.repository.LunchBoxMenuRepository;
import com.example.telegrambot.service.main.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashImpl implements CashService {
    @Autowired
    private LunchBoxMenuRepository lunchBoxMenuRepository;
    @Autowired
    private CashRepository cashRepository;

    @Autowired
    private MenuService menuService;

    @Override
    public Cash saveCash(Cash cash) {
        return cashRepository.save(cash);
    }

    @Override
    public List<Cash> getCashChatId(Long chatId) {
        return cashRepository.findByChatId(chatId);
    }

    @Override
    public void clearCash(Long chatId) {
        cashRepository.deleteByChatId(chatId);
    }

    @Override
    public boolean updateQuantity(Long chatId, Long menuId, Integer quantity) {
        var menuOptional = lunchBoxMenuRepository.findById(menuId);
        var allCount = cashRepository.getAllCount(menuId);
        if (menuOptional.isPresent()) {
            var menu = menuOptional.get();
            if (menu.getQuantity() > 0) {
                var oneCash = cashRepository.getOneCash(chatId, menuId);
                if (oneCash.getQuantity() == 1 && quantity < 0
                        || menu.getQuantity() - oneCash.getQuantity() <= 0 && quantity > 0) {
                    return false;
                }
                oneCash.setQuantity(oneCash.getQuantity() + quantity);
                cashRepository.save(oneCash);
            }
        }
        return true;
    }

    @Override
    public Cash getByChatIdAndMenuId(Long chatId, Long menuId) {
        return cashRepository.getOneCash(chatId, menuId);
    }

    @Override
    public boolean updateStatus(Long chatId, Long menuId, String status) {
        var cash = cashRepository.getOneCash(chatId, menuId);
        LunchBoxMenu menu = menuService.getMenuById(menuId);
        if (menu.getQuantity() - cash.getQuantity() < 1) {
            return false;
        }
        menu.setQuantity(menu.getQuantity() - cash.getQuantity());
        menuService.saveMenu(menu);
        cashRepository.updateStatus(chatId, menuId, status);
        return true;
    }
}
