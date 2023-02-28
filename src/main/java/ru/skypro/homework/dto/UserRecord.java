package ru.skypro.homework.dto;

import java.time.LocalDateTime;

public class UserRecord {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDateTime regDate;
    private Role role;
    private ImageRecord imageRecord;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    public ImageRecord getImageRecord() {
        return imageRecord;
    }

    public void setImage(ImageRecord imageRecord) {
        this.imageRecord = imageRecord;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setImageRecord(ImageRecord imageRecord) {
        this.imageRecord = imageRecord;
    }
}
