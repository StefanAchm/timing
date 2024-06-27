package com.asi.timer.model.view;

import com.asi.timer.backend.utils.ScoreUtil;
import com.asi.timer.enums.EnumCompetitorRoundStatus;
import com.asi.timer.enums.EnumHoldType;
import com.asi.timer.model.db.DBCompetitorRound;

import java.util.UUID;

public class APICompetitorRound {

    private UUID id;

    private EnumHoldType holdType;

    private int holdNumber;

    private int tryNumber;

    private EnumCompetitorRoundStatus competitorRoundStatus;

    private APICompetitor competitor;

    private Double score;

    private int roundNumber;

    public static APICompetitorRound fromDBCompetitorRound(DBCompetitorRound competitorRound, boolean deep) {

        APICompetitorRound apiCompetitorRound = new APICompetitorRound();

        apiCompetitorRound.setId(competitorRound.getId());
        apiCompetitorRound.setHoldType(competitorRound.getHoldType());
        apiCompetitorRound.setHoldNumber(competitorRound.getHoldNumber());
        apiCompetitorRound.setTryNumber(competitorRound.getTryNumber());
        apiCompetitorRound.setCompetitorRoundStatus(competitorRound.getCompetitorRoundStatus());
        apiCompetitorRound.setRoundNumber(competitorRound.getRound().getRoundNumber());

        if(competitorRound.getCompetitorRoundStatus().equals(EnumCompetitorRoundStatus.COMPLETED)) {
            apiCompetitorRound.setScore(ScoreUtil.calculateScore(competitorRound.toBackendCompetitorRound()));
        } else {
            apiCompetitorRound.setScore(null);
        }

        if(deep) {
            apiCompetitorRound.setCompetitor(APICompetitor.fromDBCompetitor(competitorRound.getCompetitor(), false));
        }


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

    public APICompetitor getCompetitor() {
        return competitor;
    }

    public void setCompetitor(APICompetitor competitor) {
        this.competitor = competitor;
    }

    public EnumCompetitorRoundStatus getCompetitorRoundStatus() {
        return competitorRoundStatus;
    }

    public void setCompetitorRoundStatus(EnumCompetitorRoundStatus competitorRoundStatus) {
        this.competitorRoundStatus = competitorRoundStatus;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }
}
