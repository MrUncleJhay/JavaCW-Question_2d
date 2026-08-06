package com.exhibition.model;

import java.time.LocalDate;

public class Participant {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String category;
    private LocalDate registrationDate;

    public Participant() {}

    public Participant(String fullName, String email, String phone, String category, LocalDate registrationDate) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.category = category;
        this.registrationDate = registrationDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
}
