package com.asi.timer.model.view;

import com.asi.timer.enums.EnumHoldType;
import com.asi.timer.model.db.DBCompetitorRound;

import java.util.UUID;

public class APICompetitorRound {

    private UUID id;

    private EnumHoldType holdType;

    private int holdNumber;

    private int tryNumber;


    public static APICompetitorRound fromDBCompetitorRound(DBCompetitorRound competitorRound) {

        APICompetitorRound apiCompetitorRound = new APICompetitorRound();

        apiCompetitorRound.setId(competitorRound.getId());
        apiCompetitorRound.setHoldType(competitorRound.getHoldType());
        apiCompetitorRound.setHoldNumber(competitorRound.getHoldNumber());
        apiCompetitorRound.setTryNumber(competitorRound.getTryNumber());

        return apiCompetitorRound;


    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

}
