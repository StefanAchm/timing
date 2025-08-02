package com.asi.timer.backend.model;

import com.asi.timer.enums.EnumGender;

import java.util.UUID;

public class Round {

    private UUID id;

    private int roundNumber;
    private int maxHolds;
    private EnumGender gender;

    public Round(UUID id, int roundNumber, int maxHolds, EnumGender gender) {
        this.id = id;
        this.roundNumber = roundNumber;
        this.maxHolds = maxHolds;
        this.gender = gender;
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

    public EnumGender getGender() {
        return gender;
    }

    public void setGender(EnumGender gender) {
        this.gender = gender;
    }
}
