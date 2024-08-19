package com.asi.timer.backend.model;

import com.asi.timer.enums.EnumGender;

public class Round {

    private int roundNumber;
    private int maxHolds;
    private EnumGender gender;

    public Round(int roundNumber, int maxHolds, EnumGender gender) {
        this.roundNumber = roundNumber;
        this.maxHolds = maxHolds;
        this.gender = gender;
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

    public EnumGender getGender() {
        return gender;
    }

    public void setGender(EnumGender gender) {
        this.gender = gender;
    }
}
