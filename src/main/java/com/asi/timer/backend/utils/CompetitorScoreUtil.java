package com.asi.timer.backend.utils;

import com.asi.timer.backend.model.CompetitorRound;
import com.asi.timer.backend.model.CompetitorScore;

import java.util.List;

public class CompetitorScoreUtil {

    public static List<CompetitorScore> fromCompetitorRound(List<CompetitorRound> competitorRound) {

        List<CompetitorScore> competitorScores = competitorRound.stream()
                .map(CompetitorScoreUtil::fromCompetitorRound)
                .sorted((o1, o2) -> Double.compare(o2.getScore(), o1.getScore()))
                .toList();

        // Set rank for each competitorScore
        // If two competitorScores have the same score, they should have the same rank

        for (int i = 0; i < competitorScores.size(); i++) {
            CompetitorScore competitorScore = competitorScores.get(i);
            competitorScore.setRank(i + 1);
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
