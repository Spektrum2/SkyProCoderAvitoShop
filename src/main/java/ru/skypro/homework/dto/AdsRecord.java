package ru.skypro.homework.dto;

import java.math.BigDecimal;

public class AdsRecord {
    private long id;
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String phone;
    private BigDecimal price;
    private String title;
    private ImageRecord imageRecord;
    private UserRecord userRecord;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ImageRecord getImageRecord() {
        return imageRecord;
    }

    public void setImage(ImageRecord imageRecord) {
        this.imageRecord = imageRecord;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageRecord(ImageRecord imageRecord) {
        this.imageRecord = imageRecord;
    }

    public UserRecord getUserRecord() {
        return userRecord;
    }

    public void setUserRecord(UserRecord userRecord) {
        this.userRecord = userRecord;
    }
}
