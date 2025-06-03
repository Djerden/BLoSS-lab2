package com.djeno.lab1.services;

import com.djeno.lab1.exceptions.AppAlreadyPurchasedException;
import com.djeno.lab1.persistence.models.App;
import com.djeno.lab1.persistence.models.Purchase;
import com.djeno.lab1.persistence.models.User;
import com.djeno.lab1.persistence.repositories.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PaymentMethodService paymentMethodService;

    public boolean hasUserPurchasedApp(User user, App app) {
        return purchaseRepository.existsByUserAndApp(user, app);
    }

    public Purchase purchaseApp(App app, User user) {

        // Проверяем, не куплено ли уже
        if (hasUserPurchasedApp(user, app)) {
            throw new AppAlreadyPurchasedException("Приложение уже приобретено");
        }

        // Пытаемся провести оплату
        paymentMethodService.processPayment(user, app.getPrice());

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setApp(app);
        purchase.setPurchaseDate(LocalDateTime.now());

        return purchaseRepository.save(purchase);
    }
}
