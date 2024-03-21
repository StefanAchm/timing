package com.asi.timer.model.db;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "rounds")
public class DBRound {

    @Id
    @GeneratedValue
    private UUID id;

    private int roundNumber;

    private int maxHolds;

    private String gender;

    @OneToMany(mappedBy = "round")
    private Set<DBCompetitorRound> competitorRounds;

    public DBRound() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getMaxHolds() {
        return maxHolds;
    }

    public void setMaxHolds(int maxHolds) {
        this.maxHolds = maxHolds;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Set<DBCompetitorRound> getCompetitorRounds() {
        return competitorRounds;
    }

    public void setCompetitorRounds(Set<DBCompetitorRound> competitorRounds) {
        this.competitorRounds = competitorRounds;
    }

}
