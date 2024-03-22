package com.asi.timer.backend.score;

import com.asi.timer.enums.EnumHoldType;
import com.asi.timer.model.db.DBCompetitorRound;

import java.util.List;

public class ScoreCalculator {

    public static double calculateScore(List<DBCompetitorRound> competitorRounds) {

        double score = 0;

        for (int i = 0; i < competitorRounds.size(); i++) {
            DBCompetitorRound competitorRound = competitorRounds.get(i);

            if(i < competitorRounds.size() - 1) {
                // There is a next round, therefore use max points here!
                score += 100;
            } else {
                score += calculateScore(competitorRound);
            }

        }

        return score;

    }

    public static double calculateScore(DBCompetitorRound competitorRound) {

        return calculateScore(competitorRound.getHoldNumber(),
                competitorRound.getHoldType(),
                competitorRound.getTryNumber());

    }

    public static double calculateScore(int holdNumber, EnumHoldType holdType, int tryNumber) {

        double points = 0;

        points += getPointsOfHoldNumber(holdNumber);

        points += getPointsOfHoldType(holdType);

        points += getPointsOfTryNumber(tryNumber);

        points = Math.round(points * 100.0) / 100.0;

        return points;

    }

    private static double getPointsOfHoldNumber(int holdNumber) {

        if(holdNumber > 100) {
            System.err.println("Hold number is greater than 100, this should not happen!");
            return 100;
        }

        return holdNumber;

    }

    protected static double getPointsOfTryNumber(int tryNumber) {

        if(tryNumber > 10) {
            System.err.println("Try number is greater than 10, this should not happen!");
            return -0.09;
        }

        return -((tryNumber - 1) * 0.01);

    }

    protected static double getPointsOfHoldType(EnumHoldType holdType) {

        if(holdType == null) {
            return 0;
        }

        return switch (holdType) {
            case TOUCHED -> -0.4;
            case SLIPPED -> -0.2;
            case HELD -> 0.0;
            case FOLLOWED -> 0.2;
            case MOVED_ON -> 0.4;
        };

    }


}
