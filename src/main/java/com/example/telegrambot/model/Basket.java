package com.example.telegrambot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cosg_basket", uniqueConstraints = {@UniqueConstraint(columnNames = {"chatId", "menuId"})})
public class Basket {

    @EmbeddedId
    private BasketIdentity basketIdentity;

    @NotNull
    private Integer quantity = 1;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status = Status.PASSIVE;
}
