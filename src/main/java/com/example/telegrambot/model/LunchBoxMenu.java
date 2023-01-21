package com.example.telegrambot.model;


import jakarta.persistence.*;
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
@Table(name = "cosg_lunch_box_menu")
public class LunchBoxMenu {

    @Id
    private Long menuId;

    private String name;

    private Double amount;

    private Integer line;

    @Enumerated(EnumType.STRING)
    private WeekDays weekday;

    private Timestamp registeredAt;

    private Timestamp modifiedAt;

    @Override
    public String toString() {
        return "LunchBoxMenu{" +
                "menuId=" + menuId +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", line=" + line +
                ", weekday=" + weekday +
                ", registeredAt=" + registeredAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}
