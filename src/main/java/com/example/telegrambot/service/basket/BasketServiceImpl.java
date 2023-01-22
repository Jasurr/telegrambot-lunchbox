package com.example.telegrambot.service.basket;

import com.example.telegrambot.model.Basket;
import com.example.telegrambot.model.LunchBoxMenu;
import com.example.telegrambot.model.Status;
import com.example.telegrambot.repository.BasketRepository;
import com.example.telegrambot.repository.LunchBoxMenuRepository;
import com.example.telegrambot.service.main.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasketServiceImpl implements BasketService {
    @Autowired
    private LunchBoxMenuRepository lunchBoxMenuRepository;
    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private MenuService menuService;

    @Override
    public Basket saveBasket(Basket basket) {
        Basket oneBasket = basketRepository.getOneBasket(basket.getBasketIdentity().getChatId(), basket.getBasketIdentity().getMenuId());

        if (oneBasket != null) {
            basket.setQuantity(oneBasket.getQuantity() + basket.getQuantity());
        }
        return basketRepository.save(basket);
    }

    @Override
    public List<Basket> getCashChatId(Long chatId) {
        return basketRepository.findByChatId(chatId);
    }

    @Override
    public void clearBasket(Long chatId) {
        basketRepository.deleteByChatId(chatId);
    }

    @Override
    public boolean updateQuantity(Long chatId, Long menuId, Integer quantity) {
        var menuOptional = lunchBoxMenuRepository.findById(menuId);
        var allCount = basketRepository.getAllCount(menuId);
        if (menuOptional.isPresent()) {
            var menu = menuOptional.get();
            if (menu.getQuantity() > 0) {
                var oneBasket = basketRepository.getOneBasket(chatId, menuId);
                if (oneBasket.getQuantity() == 1 && quantity < 0
                        || menu.getQuantity() - oneBasket.getQuantity() <= 0 && quantity > 0) {
                    return false;
                }
                oneBasket.setQuantity(oneBasket.getQuantity() + quantity);
                basketRepository.save(oneBasket);
            }
        }
        return true;
    }

    @Override
    public Basket getByChatIdAndMenuId(Long chatId, Long menuId) {
        return basketRepository.getOneBasket(chatId, menuId);
    }

    @Override
    public boolean updateStatus(Long chatId, Long menuId, String status) {
        var basket = basketRepository.getOneBasket(chatId, menuId);
        if (basket != null) {
            LunchBoxMenu menu = menuService.getMenuById(menuId);
            if (menu.getQuantity() - basket.getQuantity() < 1) {
                return false;
            }
            menu.setQuantity(menu.getQuantity() - basket.getQuantity());
            menuService.saveMenu(menu);
            basket.setStatus(Status.valueOf(status));
            basketRepository.save(basket);
            return true;
        }
        return false;
    }
}
