package com.asi.timer.model.db;

import com.asi.timer.enums.EnumGender;
import com.asi.timer.model.view.APICompetitor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "competitors")
public class DBCompetitor {

    @Id
    @GeneratedValue
    private UUID id;

    private int startNumber;

    private String firstName;

    private String lastName;

    private String city;

    private String club;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private EnumGender gender;

    private boolean deleted;

    @OneToMany(mappedBy = "competitor")
    private Set<DBCompetitorRound> competitorRounds;

    public DBCompetitor() {
    }

    public static DBCompetitor fromAPICompetitor(APICompetitor competitor) {

        DBCompetitor dbCompetitor = new DBCompetitor();
        dbCompetitor.setId(competitor.getId());
        dbCompetitor.setStartNumber(competitor.getStartNumber());
        dbCompetitor.setFirstName(competitor.getFirstName());
        dbCompetitor.setLastName(competitor.getLastName());
        dbCompetitor.setClub(competitor.getClub());
        dbCompetitor.setCity(competitor.getCity());
        dbCompetitor.setDateOfBirth(competitor.getDateOfBirth());
        dbCompetitor.setGender(competitor.getGender());

        return dbCompetitor;

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

    public EnumGender getGender() {
        return gender;
    }

    public void setGender(EnumGender gender) {
        this.gender = gender;
    }

    public Set<DBCompetitorRound> getCompetitorRounds() {
        return competitorRounds;
    }

    public void setCompetitorRounds(Set<DBCompetitorRound> competitorRounds) {
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
