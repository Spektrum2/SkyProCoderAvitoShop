package ru.skypro.homework.dto;

import java.time.LocalDateTime;

public class CommentRecord {
        private long id;
        private LocalDateTime createdAt;
        private String text;
        private UserRecord userRecord;

        public long getId() {
                return id;
        }

        public void setId(long id) {
                this.id = id;
        }

        public LocalDateTime getCreatedAt() {
                return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
                this.createdAt = createdAt;
        }

        public String getText() {
                return text;
        }

        public void setText(String text) {
                this.text = text;
        }

        public UserRecord getUserRecord() {
                return userRecord;
        }

        public void setUserRecord(UserRecord userRecord) {
                this.userRecord = userRecord;
        }
}
