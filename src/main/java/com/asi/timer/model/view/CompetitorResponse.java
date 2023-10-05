package com.asi.timer.model.view;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class CompetitorResponse {

    private UUID id;

    private int startNumber;

    private String firstName;

    private String lastName;

    private String domicil;

    private String club;

    private LocalDate dateOfBirth; // TODO: check datatype

    private String gender;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(int startNumber) {
        this.startNumber = startNumber;
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

    public String getDomicil() {
        return domicil;
    }

    public void setDomicil(String domicil) {
        this.domicil = domicil;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
