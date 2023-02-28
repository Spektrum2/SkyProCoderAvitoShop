package ru.skypro.homework.dto;

public class Comment{
        private Integer author;
        private String createdAt;
        private Integer id;
        private String text;

        public Integer getAuthor() {
                return author;
        }

        public void setAuthor(Integer author) {
                this.author = author;
        }

        public String getCreatedAt() {
                return createdAt;
        }

        public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
        }

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getText() {
                return text;
        }

        public void setText(String text) {
                this.text = text;
        }
}
