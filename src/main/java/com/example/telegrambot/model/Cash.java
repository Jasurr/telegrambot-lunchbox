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
@Table(name = "cosg_cash", uniqueConstraints = {@UniqueConstraint(columnNames = {"chatId", "menuId"})})
public class Cash {

    @EmbeddedId
    private CashIdentity cashIdentity;

    @NotNull
    private Integer quantity = 1;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status = Status.PASSIVE;
}
