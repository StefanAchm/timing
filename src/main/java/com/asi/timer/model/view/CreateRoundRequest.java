package com.asi.timer.model.view;

public class CreateRoundRequest {

    private RoundRequest roundRequest;

    private Score score;

    public RoundRequest getRoundRequest() {
        return roundRequest;
    }

    public void setRoundRequest(RoundRequest roundRequest) {
        this.roundRequest = roundRequest;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }
}
