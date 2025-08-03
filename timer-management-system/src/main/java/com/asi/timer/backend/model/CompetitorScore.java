package com.asi.timer.backend.model;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CompetitorScore implements Comparable<CompetitorScore> {

    private int rank;

    private Competitor competitor;

    private List<CompetitorRound> competitorRounds;

    private Scores scores;

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
        return String.format("%.4f", scores.getTotalScore());
    }

    public Scores getScores() {
        return scores;
    }

    public void setScores(Scores scores) {
        this.scores = scores;
    }

    public double getScore() {
        return scores.getTotalScore();
    }

    public double getScore(int roundNumber) {
        Map<Integer, Double> roundScores = scores.getRoundScores();
        if (roundScores == null || roundScores.isEmpty()) {
            return 0.0;
        }
        return roundScores.getOrDefault(roundNumber, 0.0);
    }

    public boolean hasSameRank(CompetitorScore currentCompetitorScore) {
        return this.compareTo(currentCompetitorScore) == 0;
    }

    @Override
    public int compareTo(CompetitorScore o) {

        Map<Integer, Double> roundScores = this.scores.getRoundScores();
        Map<Integer, Double> roundScoresOther = o.scores.getRoundScores();

        // First, compare based on the number of rounds (longer participation ranks higher)
        int roundComparison = Integer.compare(roundScores.size(), roundScoresOther.size());
        if (roundComparison != 0) {
            return -roundComparison; // More rounds -> higher rank
        }

        // If same number of rounds, compare lexicographically (last round first)
        for(int i = roundScores.size(); i > 0; i--) {
            int scoreComparison = Double.compare(roundScores.get(i), roundScoresOther.get(i));
            if (scoreComparison != 0) {
                return -scoreComparison; // Higher score -> higher rank
            }
        }

        return 0; // Equal

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompetitorScore that = (CompetitorScore) o;
        return rank == that.rank &&
                Objects.equals(competitor, that.competitor) &&
                Objects.equals(competitorRounds, that.competitorRounds) &&
                Objects.equals(scores, that.scores);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, competitor, competitorRounds, scores);
    }

    @Override
    public String toString() {
        return "CompetitorScore{" +
               "rank=" + rank +
               ", competitor=" + competitor +
               ", competitorRounds=" + competitorRounds +
               ", scores=" + scores +
               '}';
    }

}
