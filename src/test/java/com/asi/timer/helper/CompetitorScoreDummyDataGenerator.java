package com.asi.timer.helper;

import com.asi.timer.backend.score.model.CompetitorScore;
import com.asi.timer.model.db.Competitor;
import com.asi.timer.model.db.CompetitorRound;
import com.asi.timer.model.db.Round;

import java.util.Collections;

public class CompetitorScoreDummyDataGenerator {

    public static CompetitorScore generateCompetitorScore(
            int rank,
            Competitor competitor,
            Round round,
            double score) {

        CompetitorRound lastRound = new CompetitorRound();
        lastRound.setRound(round);
        lastRound.setCompetitor(competitor);

        round.setCompetitorRounds(Collections.singleton(lastRound));
        competitor.setCompetitorRounds(Collections.singleton(lastRound));

        CompetitorScore competitorScore = new CompetitorScore();
        competitorScore.setRank(rank);
        competitorScore.setCompetitor(competitor);
        competitorScore.setLastRound(lastRound);
        competitorScore.setScore(score);

        return competitorScore;

    }

}
