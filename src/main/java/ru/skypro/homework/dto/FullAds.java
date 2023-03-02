package ru.skypro.homework.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class FullAds {
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String image;
    private String phone;
    private int id;
    private int price;
    private String title;
}
