package com.asi.timer.backend.utils;

import com.asi.timer.backend.model.Competitor;
import com.asi.timer.backend.model.CompetitorRound;
import com.asi.timer.backend.model.CompetitorScore;
import com.asi.timer.backend.model.Round;

import java.util.*;

public class CompetitorScoreUtil {

    private CompetitorScoreUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * The following logic should be implemented:
     * 1. Each competitor has one CompetitorScore
     * 2. The competitorScore should be calculated in the following way:
     *
     * @param competitorRounds
     * @return
     */
    public static List<CompetitorScore> fromCompetitorRounds2(List<CompetitorRound> competitorRounds,
                                                              List<Round> rounds

    ) {

        Map<Competitor, List<CompetitorRound>> competitorRoundMap = getCompetitorRoundMap(competitorRounds);

        List<CompetitorScore> competitorScores = new ArrayList<>();

        for (Map.Entry<Competitor, List<CompetitorRound>> entry : competitorRoundMap.entrySet()) {

            double score = ScoreUtil.calculateTotalScore(entry.getValue(), rounds);

            CompetitorScore competitorScore = new CompetitorScore();
            competitorScore.setCompetitor(entry.getKey());
            competitorScore.setScore(score);

            CompetitorRound lastRound = entry.getValue().stream()
                    .max(Comparator.comparing(CompetitorRound::getRoundNumber))
                    .orElseThrow();

            competitorScore.setLastRound(lastRound);
            competitorScore.setRank(0);

            competitorScores.add(competitorScore);


        }

        // Set rank for each competitorScore

        // sort competitorScores by score and update rank

        competitorScores.sort((o1, o2) -> Double.compare(o2.getScore(), o1.getScore()));

        for(int i = 0; i < competitorScores.size(); i++) {
            competitorScores.get(i).setRank(i + 1);
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

    public static List<CompetitorScore> fromCompetitorRounds(List<CompetitorRound> competitorRounds) {

        List<CompetitorScore> competitorScores = competitorRounds.stream()
                .map(CompetitorScoreUtil::fromCompetitorRound)
                .sorted((o1, o2) -> Double.compare(o2.getScore(), o1.getScore()))
                .toList();

        // Set rank for each competitorScore
        // If two competitorScores have the same score, they should have the same rank

        for (int i = 0; i < competitorScores.size(); i++) {
            competitorScores.get(i).setRank(i + 1);
        }

        return competitorScores;

    }

    public static CompetitorScore fromCompetitorRound(CompetitorRound competitorRound) {

        CompetitorScore competitorScore = new CompetitorScore();

        competitorScore.setScore(ScoreUtil.calculateScore(competitorRound));

        competitorScore.setLastRound(competitorRound);
        competitorScore.setCompetitor(competitorRound.getCompetitor());

        return competitorScore;

    }

}
