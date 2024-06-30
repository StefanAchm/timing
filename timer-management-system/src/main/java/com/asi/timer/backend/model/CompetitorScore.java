package com.asi.timer.backend.model;

import java.util.Comparator;
import java.util.List;

public class CompetitorScore {

    private int rank;

    private Competitor competitor;

    private List<CompetitorRound> competitorRounds;

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
        // return the one with the highest round number
        return this.competitorRounds
                .stream()
                .max(Comparator.comparingInt(CompetitorRound::getRoundNumber))
                .orElse(null);
    }

    public List<CompetitorRound> getCompetitorRounds() {
        return competitorRounds;
    }

    public void setCompetitorRounds(List<CompetitorRound> competitorRounds) {
        this.competitorRounds = competitorRounds;
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
