package com.asi.timer.backend.model;

public class Round {

    private int roundNumber;
    private int maxHolds;

    public Round(int roundNumber, int maxHolds) {
        this.roundNumber = roundNumber;
        this.maxHolds = maxHolds;
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
}
