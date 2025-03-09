package com.asi.timer.model.view;

import com.asi.timer.backend.model.CompetitorRound;
import com.asi.timer.backend.model.Round;
import com.asi.timer.backend.utils.ScoreUtil;
import com.asi.timer.enums.EnumGender;
import com.asi.timer.enums.EnumPaymentStatus;
import com.asi.timer.model.db.DBCompetitor;
import com.asi.timer.model.db.DBCompetitorRound;
import com.asi.timer.model.db.DBRound;

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

    private double totalScore;

    private EnumPaymentStatus paymentStatus;

    private List<APICompetitorRound> competitorRounds;

    public APICompetitor() {
        this.competitorRounds = new ArrayList<>();
    }

    public static APICompetitor fromDBCompetitor(DBCompetitor competitor, boolean deep) {

        APICompetitor apiCompetitor = new APICompetitor();
        apiCompetitor.setId(competitor.getId());
        apiCompetitor.setStartNumber(competitor.getStartNumber());
        apiCompetitor.setFirstName(competitor.getFirstName());
        apiCompetitor.setLastName(competitor.getLastName());
        apiCompetitor.setClub(competitor.getClub());
        apiCompetitor.setCity(competitor.getCity());
        apiCompetitor.setDateOfBirth(competitor.getDateOfBirth());
        apiCompetitor.setGender(competitor.getGender());
        apiCompetitor.setPaymentStatus(competitor.getPaymentStatus());

        if(deep) {

            competitor.getCompetitorRounds()
                    .forEach(competitorRound -> apiCompetitor.getCompetitorRounds().add(
                            APICompetitorRound.fromDBCompetitorRound(competitorRound, false))
                    );

            List<CompetitorRound> competitorRounds = competitor.getCompetitorRounds()
                    .stream()
                    .map(DBCompetitorRound::toBackendCompetitorRound)
                    .toList();

            List<Round> rounds = competitor.getCompetitorRounds()
                    .stream()
                    .map(DBCompetitorRound::getRound)
                    .map(DBRound::toBackendRound)
                    .toList();

            apiCompetitor.setTotalScore(ScoreUtil.calculateScoreOfAllRounds(competitorRounds, rounds).getTotalScore());

        }

        return apiCompetitor;

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

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public EnumPaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(EnumPaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
