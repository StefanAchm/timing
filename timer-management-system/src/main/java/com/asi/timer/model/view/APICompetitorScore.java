package com.asi.timer.model.view;

import com.asi.timer.backend.model.CompetitorRound;
import com.asi.timer.backend.model.CompetitorScore;

import java.util.List;
import java.util.UUID;

public class APICompetitorScore {

    private record APICompetitorRoundScore(int lastRound, int holdNumber, String holdType, int tryNumber, double points) {
    }

    private int rank;
    private UUID id;
    private String firstName;
    private String lastName;
    private String city;
    private String club;
    private int startNumber;
    private int lastRound;
    private int holdNumber;
    private String holdType;
    private int tryNumber;
    private double points;

    private List<APICompetitorRoundScore> competitorRoundScores;

    private APICompetitorScore() {
    }


    public static APICompetitorScore fromCompetitorScores(CompetitorScore competitorScores) {

        APICompetitorScore apiCompetitorScore = new APICompetitorScore();

        apiCompetitorScore.setId(competitorScores.getCompetitor().getId());
        apiCompetitorScore.setRank(competitorScores.getRank());
        apiCompetitorScore.setFirstName(competitorScores.getCompetitor().getFirstName());
        apiCompetitorScore.setLastName(competitorScores.getCompetitor().getLastName());
        apiCompetitorScore.setCity(competitorScores.getCompetitor().getCity());
        apiCompetitorScore.setClub(competitorScores.getCompetitor().getClub());
        apiCompetitorScore.setStartNumber(competitorScores.getCompetitor().getStartNumber());

        CompetitorRound lastRound = competitorScores.getLastRound();

        if (lastRound != null) {
            apiCompetitorScore.setLastRound(lastRound.getRoundNumber());
            apiCompetitorScore.setHoldNumber(lastRound.getHoldNumber());

            if(lastRound.getHoldType() != null) {
                apiCompetitorScore.setHoldType(lastRound.getHoldType().toString());
            } else {
                apiCompetitorScore.setHoldType("");
            }

            apiCompetitorScore.setTryNumber(lastRound.getTryNumber());
            apiCompetitorScore.setPoints(competitorScores.getScore());
        }

        apiCompetitorScore.competitorRoundScores = competitorScores.getCompetitorRounds()
                .stream()
                .map(round -> new APICompetitorRoundScore(
                        round.getRoundNumber(),
                        round.getHoldNumber(),
                        round.getHoldType() != null ? round.getHoldType().toString() : "",
                        round.getTryNumber(),
                        competitorScores.getScore(round.getRoundNumber())
                ))
                .toList();

        return apiCompetitorScore;

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public List<APICompetitorRoundScore> getCompetitorRoundScores() {
        return competitorRoundScores;
    }

    public void setCompetitorRoundScores(List<APICompetitorRoundScore> competitorRoundScores) {
        this.competitorRoundScores = competitorRoundScores;
    }

    public int getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(int startNumber) {
        this.startNumber = startNumber;
    }
}
