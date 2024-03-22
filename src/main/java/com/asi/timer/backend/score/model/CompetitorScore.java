package com.asi.timer.backend.score.model;

import com.asi.timer.model.db.DBCompetitor;
import com.asi.timer.model.db.DBCompetitorRound;

public class CompetitorScore {

    private int rank;

    private DBCompetitor competitor;

    private DBCompetitorRound lastRound;

    private double score;

    public CompetitorScore() {
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public DBCompetitor getCompetitor() {
        return competitor;
    }

    public void setCompetitor(DBCompetitor competitor) {
        this.competitor = competitor;
    }

    public DBCompetitorRound getLastRound() {
        return lastRound;
    }

    public void setLastRound(DBCompetitorRound lastRound) {
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
