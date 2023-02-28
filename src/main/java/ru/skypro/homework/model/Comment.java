package ru.skypro.homework.model;

public class Comment {
    long id;
    int author;
    String createdAt;
    String text;

    public Comment() {
    }

    public Comment(int author, String createdAt, String text) {
        this.author = author;
        this.createdAt = createdAt;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
