package com.asi.timer.backend.score.model;

import com.asi.timer.model.db.Competitor;
import com.asi.timer.model.db.CompetitorRound;

public class CompetitorScore {

    private int rank;

    private Competitor competitor;

    private CompetitorRound lastRound;

    private double score;

    public CompetitorScore() {
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Competitor getCompetitor() {
        return competitor;
    }

    public void setCompetitor(Competitor competitor) {
        this.competitor = competitor;
    }

    public CompetitorRound getLastRound() {
        return lastRound;
    }

    public void setLastRound(CompetitorRound lastRound) {
        this.lastRound = lastRound;
    }

    /**
     * @return the score with max 3 decimal places.
     */
    public String getScoreForPdf() {
        return String.format("%.3f", score);
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
