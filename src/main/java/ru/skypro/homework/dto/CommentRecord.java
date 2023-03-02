package ru.skypro.homework.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CommentRecord {
        private String author;
        private String createdAt;
        private int id;
        private String text;
}
