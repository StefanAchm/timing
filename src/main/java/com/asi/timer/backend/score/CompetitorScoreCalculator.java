package com.asi.timer.backend.score;

import com.asi.timer.backend.score.model.CompetitorScore;
import com.asi.timer.enums.EnumCompetitorRoundStatus;
import com.asi.timer.model.db.DBCompetitorRound;

import java.util.List;

public class CompetitorScoreCalculator {

    public static List<CompetitorScore> fromCompetitorRound(List<DBCompetitorRound> competitorRound) {

        List<CompetitorScore> competitorScores = competitorRound.stream()
                .map(CompetitorScoreCalculator::fromCompetitorRound)
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

    public static CompetitorScore fromCompetitorRound(DBCompetitorRound competitorRound) {

        CompetitorScore competitorScore = new CompetitorScore();

        competitorScore.setScore(ScoreCalculator.calculateScore(competitorRound));

        competitorScore.setLastRound(competitorRound);
        competitorScore.setCompetitor(competitorRound.getCompetitor());

        return competitorScore;

    }

}
