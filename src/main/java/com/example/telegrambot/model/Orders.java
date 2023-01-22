package com.example.telegrambot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cosg_orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Long chatId;

    @NotNull
    private Long menuId;

    @NotNull
    private Double amount;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.NOT_PAID_YET;

    @NotNull
    private Timestamp registeredAt = new Timestamp(System.currentTimeMillis());

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", menuId=" + menuId +
                ", amount=" + amount +
                ", paymentStatus=" + paymentStatus +
                ", registeredAt=" + registeredAt +
                '}';
    }
}
