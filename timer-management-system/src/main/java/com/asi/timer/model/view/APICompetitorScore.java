package com.asi.timer.model.view;

import com.asi.timer.backend.model.CompetitorScore;

public class APICompetitorScore {

    private int rank;
    private String firstName;
    private String lastName;
    private String city;
    private String club;
    private int lastRound;
    private int holdNumber;
    private String holdType;
    private int tryNumber;
    private double points;

    private APICompetitorScore() {
    }


    public static APICompetitorScore fromCompetitorScores(CompetitorScore competitorScores) {

        APICompetitorScore apiCompetitorScore = new APICompetitorScore();

        apiCompetitorScore.setRank(competitorScores.getRank());
        apiCompetitorScore.setFirstName(competitorScores.getCompetitor().getFirstName());
        apiCompetitorScore.setLastName(competitorScores.getCompetitor().getLastName());
        apiCompetitorScore.setCity(competitorScores.getCompetitor().getCity());
        apiCompetitorScore.setClub(competitorScores.getCompetitor().getClub());

        if (competitorScores.getLastRound() != null) {
            apiCompetitorScore.setLastRound(competitorScores.getLastRound().getRoundNumber());
            apiCompetitorScore.setHoldNumber(competitorScores.getLastRound().getHoldNumber());
            apiCompetitorScore.setHoldType(competitorScores.getLastRound().getHoldType().toString());
            apiCompetitorScore.setTryNumber(competitorScores.getLastRound().getTryNumber());
            apiCompetitorScore.setPoints(competitorScores.getScore());
        }

        return apiCompetitorScore;

    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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

    public int getLastRound() {
        return lastRound;
    }

    public void setLastRound(int lastRound) {
        this.lastRound = lastRound;
    }

    public int getHoldNumber() {
        return holdNumber;
    }

    public void setHoldNumber(int holdNumber) {
        this.holdNumber = holdNumber;
    }

    public String getHoldType() {
        return holdType;
    }

    public void setHoldType(String holdType) {
        this.holdType = holdType;
    }

    public int getTryNumber() {
        return tryNumber;
    }

    public void setTryNumber(int tryNumber) {
        this.tryNumber = tryNumber;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }
}
