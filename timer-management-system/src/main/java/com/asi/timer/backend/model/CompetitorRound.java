package com.asi.timer.backend.model;

import com.asi.timer.enums.EnumCompetitorRoundStatus;
import com.asi.timer.enums.EnumHoldType;

import java.util.UUID;

public class CompetitorRound {

    private UUID id;

    private int roundNumber;

    private EnumHoldType holdType;

    private int holdNumber;

    private int tryNumber;

    private EnumCompetitorRoundStatus competitorRoundStatus;

    private Competitor competitor;

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

    public EnumHoldType getHoldType() {
        return holdType;
    }

    public void setHoldType(EnumHoldType holdType) {
        this.holdType = holdType;
    }

    public int getHoldNumber() {
        return holdNumber;
    }

    public void setHoldNumber(int holdNumber) {
        this.holdNumber = holdNumber;
    }

    public int getTryNumber() {
        return tryNumber;
    }

    public void setTryNumber(int tryNumber) {
        this.tryNumber = tryNumber;
    }

    public EnumCompetitorRoundStatus getCompetitorRoundStatus() {
        return competitorRoundStatus;
    }

    public void setCompetitorRoundStatus(EnumCompetitorRoundStatus competitorRoundStatus) {
        this.competitorRoundStatus = competitorRoundStatus;
    }

    public Competitor getCompetitor() {
        return competitor;
    }

    public void setCompetitor(Competitor competitor) {
        this.competitor = competitor;
    }

    @Override
    public String toString() {
        return "CompetitorRound{" +
               "roundNumber=" + roundNumber +
               ", holdType=" + holdType +
               ", holdNumber=" + holdNumber +
               ", tryNumber=" + tryNumber +
               '}';
    }
}
