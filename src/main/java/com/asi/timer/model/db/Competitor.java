package com.asi.timer.model.db;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "competitors")
public class Competitor {

    @Id
    @GeneratedValue
    private UUID id;

    private int startNumber;

    private String firstName;

    private String lastName;

    private String city;

    private String club;

    private LocalDate dateOfBirth; // TODO: check datatype

    private String gender;

    @OneToMany(mappedBy = "competitor")
    private Set<CompetitorRound> competitorRounds;

    public Competitor() {
    }

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public Set<CompetitorRound> getCompetitorRounds() {
        return competitorRounds;
    }

    public void setCompetitorRounds(Set<CompetitorRound> competitorRounds) {
        this.competitorRounds = competitorRounds;
    }

    // Custom

    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     *
     * @return the date like dd-mm-yyyy
     */
    public String getDateOfBirthAsString() {

        return dateOfBirth.getDayOfMonth() + "-" + dateOfBirth.getMonthValue() + "-" + dateOfBirth.getYear();

    }

}
