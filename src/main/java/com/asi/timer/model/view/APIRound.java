package com.asi.timer.model.view;

import com.asi.timer.enums.EnumGender;
import com.asi.timer.model.db.DBRound;

import java.util.UUID;

public class APIRound {

    private UUID id;

    private int roundNumber;

    private int maxHolds;

    private EnumGender gender;

    private int successScore;

    private APIScore apiScore;

    private int numberOfCompetitors;

    public static APIRound fromDBRound(DBRound dbRound) {

        APIRound apiRound = new APIRound();

        apiRound.setId(dbRound.getId());
        apiRound.setRoundNumber(dbRound.getRoundNumber());
        apiRound.setMaxHolds(dbRound.getMaxHolds());
        apiRound.setGender(dbRound.getGender());

        apiRound.setNumberOfCompetitors(dbRound.getCompetitorRounds().size());

        return apiRound;

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

    public int getSuccessScore() {
        return successScore;
    }

    public void setSuccessScore(int successScore) {
        this.successScore = successScore;
    }

    public APIScore getApiScore() {
        return apiScore;
    }

    public void setApiScore(APIScore apiScore) {
        this.apiScore = apiScore;
    }

    public int getNumberOfCompetitors() {
        return numberOfCompetitors;
    }

    public void setNumberOfCompetitors(int numberOfCompetitors) {
        this.numberOfCompetitors = numberOfCompetitors;
    }
}
