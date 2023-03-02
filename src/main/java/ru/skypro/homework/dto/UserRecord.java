package ru.skypro.homework.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserRecord {
    private String email;
    private String firstName;
    private int id;
    private String lastName;
    private String phone;
    private String regDate;
    private String image;
}
