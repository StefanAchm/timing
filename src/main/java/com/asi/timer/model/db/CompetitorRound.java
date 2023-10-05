package com.asi.timer.model.db;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "competitor_rounds")
public class CompetitorRound {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Competitor competitor;

    @ManyToOne
    private Round round;

    private int score; // TODO: extend with hold, etc.

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public Competitor getCompetitor() {
        return competitor;
    }

    public void setCompetitor(Competitor competitor) {
        this.competitor = competitor;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
