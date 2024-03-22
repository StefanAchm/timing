package com.asi.timer.helper;

import com.asi.timer.backend.score.model.CompetitorScore;
import com.asi.timer.model.db.DBCompetitor;
import com.asi.timer.model.db.DBCompetitorRound;
import com.asi.timer.model.db.DBRound;

import java.util.Collections;

public class CompetitorScoreDummyDataGenerator {

    public static CompetitorScore generateCompetitorScore(
            int rank,
            DBCompetitor competitor,
            DBRound round,
            double score) {

        DBCompetitorRound lastRound = new DBCompetitorRound();
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
