package com.asi.timer.backend.utils;

import com.asi.timer.backend.model.*;
import com.asi.timer.enums.EnumCompetitorRoundStatus;
import com.asi.timer.enums.EnumGender;
import com.asi.timer.enums.EnumHoldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ScoreUtil {

    private static final Logger logger = LoggerFactory.getLogger(ScoreUtil.class);

    private ScoreUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns the score for each competitor. <br>
     *
     * The following logic should be implemented:
     * 1. Each competitor has one CompetitorScore
     * 2. The competitorScore should be calculated in the following way:
     */
    public static List<CompetitorScore> getCompetitorScores(List<CompetitorRound> competitorRounds,
                                                            List<Round> rounds) {

        Map<Competitor, List<CompetitorRound>> competitorRoundMap = getCompetitorRoundMap(competitorRounds);

        List<CompetitorScore> competitorScores = getCompetitorScores(rounds, competitorRoundMap);

        if(competitorScores.isEmpty()) {
            return competitorScores;
        }

        updateScoresWithPreviousRoundsIfNeeded(competitorScores);

        updateRank(competitorScores);

        return competitorScores;

    }

    private static void updateRank(List<CompetitorScore> competitorScores) {

        // sort competitorScores by score
        competitorScores.sort((o1, o2) -> Double.compare(o2.getScore(), o1.getScore()));

        // Set rank for each competitorScore (if two competitors have the same score, they should have the same rank)
        double lastScore = competitorScores.get(0).getScore();
        int rank = 1;
        for (CompetitorScore competitorScore : competitorScores) {

            double currentScore = competitorScore.getScore();

            if(currentScore < lastScore) {
                rank++; // Increase rank if score is lower than the last score
            } // If the score is the same, the rank stays the same

            competitorScore.setRank(rank);

            lastScore = currentScore;

        }

    }

    /**
     * Update the scores of competitors with the same score to distinguish them. <br>
     * E.g. if two competitors have the same score (same score in the last round), check if the scores of previous rounds are different.
     * @param competitorScores List of CompetitorScores
     */
    private static void updateScoresWithPreviousRoundsIfNeeded(List<CompetitorScore> competitorScores) {

        // First, sort the competitorScores by score
        Collections.sort(competitorScores);

        CompetitorScore lastCompetitorScore = competitorScores.get(competitorScores.size() - 1);
        double scoreShifter = 0.0001;
        for(int i = competitorScores.size() - 1; i >= 0; i--) {

            CompetitorScore currentCompetitorScore = competitorScores.get(i);

            if(!lastCompetitorScore.hasSameRank(currentCompetitorScore) && lastCompetitorScore.getScore() == currentCompetitorScore.getScore()) {
                competitorScores.get(i).getScores().addToTotalScore(scoreShifter);
                scoreShifter += 0.0001;
            } else {
                scoreShifter = 0.0001;
            }

            lastCompetitorScore = currentCompetitorScore;

        }
    }

    private static List<CompetitorScore> getCompetitorScores(List<Round> rounds,
                                                             Map<Competitor, List<CompetitorRound>> competitorRoundMap) {

        List<CompetitorScore> competitorScores = new ArrayList<>();

        for (Map.Entry<Competitor, List<CompetitorRound>> entry : competitorRoundMap.entrySet()) {

            Scores scores = calculateScoreOfAllRounds(entry.getValue(), rounds);

            CompetitorScore competitorScore = new CompetitorScore();
            competitorScore.setCompetitor(entry.getKey());
            competitorScore.setScores(scores);
            competitorScore.setCompetitorRounds(entry.getValue());
            competitorScore.setRank(0);

            competitorScores.add(competitorScore);

        }

        return competitorScores;

    }

    private static Map<Competitor, List<CompetitorRound>> getCompetitorRoundMap(List<CompetitorRound> competitorRounds) {

        Map<Competitor, List<CompetitorRound>> competitorRoundMap = new HashMap<>();

        for (CompetitorRound competitorRound : competitorRounds) {

            if (!competitorRoundMap.containsKey(competitorRound.getCompetitor())) {
                competitorRoundMap.put(competitorRound.getCompetitor(), new ArrayList<>());
            }

            competitorRoundMap.get(competitorRound.getCompetitor()).add(competitorRound);

        }

        return competitorRoundMap;

    }

    /**
     * Calculates the total score of a competitor. <br>
     * @param competitorRounds List of CompetitorRounds of this competitor
     * @param rounds List of all rounds
     * @return the total score of the competitor
     */
    public static Scores calculateScoreOfAllRounds(List<CompetitorRound> competitorRounds, List<Round> rounds) {



        int lastRound = competitorRounds.stream()
                .map(CompetitorRound::getRoundNumber)
                .max(Integer::compareTo)
                .orElseThrow(() -> new RuntimeException("No round found"));

        Scores scores = new Scores();
        Map<Integer, Double> roundScores = new HashMap<>();
        double totalScore = 0;

        for (CompetitorRound competitorRound : competitorRounds) {

            int roundNumber = competitorRound.getRoundNumber();
            EnumGender gender = competitorRound.getCompetitor().getGender();

            boolean isLastRound = competitorRound.getRoundNumber() == lastRound;

            double score = calculateScoreOfRound(competitorRound);

            if (isLastRound) {

                totalScore += score;

            } else {

                int maxHolds = rounds.stream()
                        .filter(round -> round.getGender().equals(gender))
                        .filter(round -> round.getRoundNumber() == roundNumber)
                        .findFirst()
                        .map(Round::getMaxHolds)
                        .orElseThrow(() -> new RuntimeException("Round not found"));

                totalScore += maxHolds;

            }

            roundScores.put(roundNumber, score);

        }

        scores.setTotalScore(totalScore);
        scores.setRoundScores(roundScores);

        return scores;

    }

    public static double calculateScoreOfRound(CompetitorRound competitorRound) {

        if(competitorRound.getCompetitorRoundStatus().equals(EnumCompetitorRoundStatus.COMPLETED)) {

            return getPoints(
                    competitorRound.getHoldNumber(),
                    competitorRound.getHoldType(),
                    competitorRound.getTryNumber()
            );

        } else {
            return 0;
        }

    }

    /**
     * The points are calculated in the following way:
     * 1. The hold number determines the integer part of the points
     * 2. The hold type determines the decimal part of the points (touch = -0.4, slip = -0.2, hold = 0.0, follow = 0.2, move on = 0.4)
     * 3. The try number determines the deduction of the points (-0.01 for each try)
     */
    public static double getPoints(int holdNumber, EnumHoldType holdType, int tryNumber) {

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
