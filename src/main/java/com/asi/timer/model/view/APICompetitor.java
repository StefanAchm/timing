package com.asi.timer.model.view;

import com.asi.timer.enums.EnumGender;
import com.asi.timer.model.db.DBCompetitor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class APICompetitor {


    private UUID id;

    private int startNumber;

    private String firstName;

    private String lastName;

    private String city;

    private String club;

    private LocalDate dateOfBirth;

    private EnumGender gender;

    private List<APICompetitorRound> competitorRounds;

    public APICompetitor() {
        this.competitorRounds = new ArrayList<>();
    }

    public static APICompetitor fromDBCompetitorRound(DBCompetitor competitor) {

        APICompetitor competitorResponse = new APICompetitor();
        competitorResponse.setId(competitor.getId());
        competitorResponse.setStartNumber(competitor.getStartNumber());
        competitorResponse.setFirstName(competitor.getFirstName());
        competitorResponse.setLastName(competitor.getLastName());
        competitorResponse.setClub(competitor.getClub());
        competitorResponse.setCity(competitor.getCity());
        competitorResponse.setDateOfBirth(competitor.getDateOfBirth());
        competitorResponse.setGender(competitor.getGender());

        competitor.getCompetitorRounds().forEach(competitorRound -> competitorResponse.getCompetitorRounds().add(
                APICompetitorRound.fromDBCompetitorRound(competitorRound))
        );

        return competitorResponse;

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

    public List<APICompetitorRound> getCompetitorRounds() {
        return competitorRounds;
    }

    public void setCompetitorRounds(List<APICompetitorRound> competitorRounds) {
        this.competitorRounds = competitorRounds;
    }
}
