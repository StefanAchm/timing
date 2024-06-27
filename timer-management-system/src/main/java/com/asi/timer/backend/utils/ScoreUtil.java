package com.asi.timer.backend.utils;

import com.asi.timer.backend.model.CompetitorRound;
import com.asi.timer.backend.model.Round;
import com.asi.timer.enums.EnumHoldType;

import java.util.List;

public class ScoreUtil {

    @Deprecated
    public static double calculateTotalScore(List<CompetitorRound> competitorRounds, List<Round> rounds) {

        double score = 0;

        for (int i = 0; i < competitorRounds.size(); i++) {
            CompetitorRound competitorRound = competitorRounds.get(i);

            if(i < competitorRounds.size() - 1) {

                int maxHolds = rounds.stream()
                        .filter(round -> round.getRoundNumber() == competitorRound.getRoundNumber())
                        .findFirst()
                        .map(Round::getMaxHolds)
                        .orElseThrow(() -> new RuntimeException("Round not found"));

                score += maxHolds;

            } else {
                score += calculateScore(competitorRound);
            }

        }

        return score;

    }

    public static double calculateScore(CompetitorRound competitorRound) {

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
