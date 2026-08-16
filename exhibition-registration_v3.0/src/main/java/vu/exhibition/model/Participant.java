package vu.exhibition.model;

import java.time.LocalDate;

/**
 * Plain data holder for a single exhibition participant. Mirrors the
 * {@code participants} table one-to-one and carries no validation or
 * persistence logic of its own — see
 * {@link vu.exhibition.util.ValidationUtils} for validation and
 * {@link vu.exhibition.dao.ParticipantDAO} for persistence.
 */
public class Participant {

    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String category;
    private LocalDate registrationDate;

    /** No-arg constructor for incremental building via setters. */
    public Participant() {
    }

    /**
     * Constructs a new, not-yet-persisted participant. {@code id} is left
     * at 0; SQLite assigns the real id (AUTOINCREMENT) on insert.
     */
    public Participant(String fullName, String email, String phone, String category, LocalDate registrationDate) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.category = category;
        this.registrationDate = registrationDate;
    }

    /** Full constructor, e.g. for rehydrating a row read back from the database. */
    public Participant(int id, String fullName, String email, String phone, String category, LocalDate registrationDate) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.category = category;
        this.registrationDate = registrationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", category='" + category + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
