package com.asi.timer.backend.model;

import java.util.Map;

public class Scores {

    private Map<Integer, Double> roundScores;

    private double totalScore;

    public Map<Integer, Double> getRoundScores() {
        return roundScores;
    }

    public void setRoundScores(Map<Integer, Double> roundScores) {
        this.roundScores = roundScores;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public void addToTotalScore(double score) {
        this.totalScore += score;
    }

    @Override
    public String toString() {
        return "Scores{" +
               "roundScores=" + roundScores +
               ", totalScore=" + totalScore +
               '}';
    }
}
