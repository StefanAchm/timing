package com.asi.timer.model.view;

import java.util.UUID;

public class RoundRequest {

    private UUID id;

    private int roundNumber;

    private int maxHolds;

    private String gender;

    private int successScore;

    public RoundRequest() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getMaxHolds() {
        return maxHolds;
    }

    public void setMaxHolds(int maxHolds) {
        this.maxHolds = maxHolds;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getSuccessScore() {
        return successScore;
    }

    public void setSuccessScore(int successScore) {
        this.successScore = successScore;
    }
}
