package com.asi.timer.model.view;

import com.asi.timer.enums.EnumHoldType;
import com.asi.timer.model.db.DBCompetitorRound;

import java.util.UUID;

public class CompetitorRoundResponse {

    private UUID id;

    private EnumHoldType holdType;

    private int holdNumber;

    private int tryNumber;

    public static CompetitorRoundResponse fromDBCompetitorRound(DBCompetitorRound competitorRound) {

        CompetitorRoundResponse competitorRoundResponse = new CompetitorRoundResponse();

        competitorRoundResponse.setId(competitorRound.getId());
        competitorRoundResponse.setHoldType(competitorRound.getHoldType());
        competitorRoundResponse.setHoldNumber(competitorRound.getHoldNumber());
        competitorRoundResponse.setTryNumber(competitorRound.getTryNumber());

        return competitorRoundResponse;


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
