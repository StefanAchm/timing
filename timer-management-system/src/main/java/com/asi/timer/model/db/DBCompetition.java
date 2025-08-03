package com.asi.timer.model.db;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "competitions")
public class DBCompetition {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private DBCompetitorRound competitorRound;

    @ManyToOne
    private DBRound round;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public DBCompetitorRound getCompetitorRound() {
        return competitorRound;
    }

    public void setCompetitorRound(DBCompetitorRound competitorRound) {
        this.competitorRound = competitorRound;
    }

    public DBRound getRound() {
        return round;
    }

    public void setRound(DBRound round) {
        this.round = round;
    }
}
