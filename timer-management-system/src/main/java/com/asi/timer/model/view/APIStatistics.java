package com.asi.timer.model.view;

public class APIStatistics {

    private long competitorCount;
    private long roundCount;
    private long competitorRoundCount;

    public APIStatistics(long competitorCount, long roundCount, long competitorRoundCount) {
        this.competitorCount = competitorCount;
        this.roundCount = roundCount;
        this.competitorRoundCount = competitorRoundCount;
    }

    public long getCompetitorCount() {
        return competitorCount;
    }

    public long getRoundCount() {
        return roundCount;
    }

    public long getCompetitorRoundCount() {
        return competitorRoundCount;
    }

}
