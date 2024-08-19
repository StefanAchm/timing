package com.asi.timer.backend.utils;

import com.asi.timer.backend.model.Competitor;
import com.asi.timer.backend.model.CompetitorRound;
import com.asi.timer.backend.model.CompetitorScore;
import com.asi.timer.backend.model.Round;
import com.asi.timer.enums.EnumCompetitorRoundStatus;
import com.asi.timer.enums.EnumGender;
import com.asi.timer.enums.EnumHoldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.IntStream;

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

        List<CompetitorScore> competitorScores = getInitialCompetitorScores(rounds, competitorRoundMap);

        alignCompetitorScores(rounds, competitorScores);

        // sort competitorScores by score
        competitorScores.sort((o1, o2) -> Double.compare(o2.getScore(), o1.getScore()));

        // Set rank for each competitorScore
        IntStream.range(0, competitorScores.size()).forEach(i -> competitorScores.get(i).setRank(i + 1));

        return competitorScores;

    }

    private static List<CompetitorScore> getInitialCompetitorScores(List<Round> rounds,
                                                                    Map<Competitor, List<CompetitorRound>> competitorRoundMap) {

        List<CompetitorScore> competitorScores = new ArrayList<>();

        for (Map.Entry<Competitor, List<CompetitorRound>> entry : competitorRoundMap.entrySet()) {

            double score = calculateTotalScore(entry.getValue(), rounds);

            CompetitorScore competitorScore = new CompetitorScore();
            competitorScore.setCompetitor(entry.getKey());
            competitorScore.setScore(score);
            competitorScore.setCompetitorRounds(entry.getValue());
            competitorScore.setRank(0);

            competitorScores.add(competitorScore);

        }

        return competitorScores;

    }

    private static void alignCompetitorScores(List<Round> rounds, List<CompetitorScore> competitorScores) {

        // Go threw each round, starting by the last (highest round number)
        // Try to find competitors, which have completed this round and have the same score in this round
        // If there are multiple competitors with the same score, use the score of the previous round for comparison

        // TODO: Implement this method



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
    public static double calculateTotalScore(List<CompetitorRound> competitorRounds, List<Round> rounds) {

        double score = 0;

        int lastRound = competitorRounds.stream()
                .map(CompetitorRound::getRoundNumber)
                .max(Integer::compareTo)
                .orElseThrow(() -> new RuntimeException("No round found"));

        for (CompetitorRound competitorRound : competitorRounds) {

            int roundNumber = competitorRound.getRoundNumber();
            EnumGender gender = competitorRound.getCompetitor().getGender();

            boolean isLastRound = competitorRound.getRoundNumber() == lastRound;

            if (isLastRound) {

                score += calculateScore(competitorRound);

            } else {

                int maxHolds = rounds.stream()
                        .filter(round -> round.getGender().equals(gender))
                        .filter(round -> round.getRoundNumber() == roundNumber)
                        .findFirst()
                        .map(Round::getMaxHolds)
                        .orElseThrow(() -> new RuntimeException("Round not found"));

                score += maxHolds;

            }

        }

        return score;

    }

    public static double calculateScore(CompetitorRound competitorRound) {

        if(competitorRound.getCompetitorRoundStatus().equals(EnumCompetitorRoundStatus.COMPLETED)) {

            return calculateScore(
                    competitorRound.getHoldNumber(),
                    competitorRound.getHoldType(),
                    competitorRound.getTryNumber()
            );

        } else {
            return 0;
        }

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
