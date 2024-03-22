package com.asi.timer.model.db;

import com.asi.timer.enums.EnumCompetitorRoundStatus;
import com.asi.timer.enums.EnumHoldType;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "competitor_rounds")
public class DBCompetitorRound {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private DBCompetitor competitor;

    @ManyToOne
    private DBRound round;

    @Enumerated(EnumType.STRING)
    private EnumHoldType holdType;

    private int holdNumber;

    private int tryNumber;

    @Enumerated(EnumType.STRING)
    private EnumCompetitorRoundStatus competitorRoundStatus;

    public DBCompetitorRound() {
        this.competitorRoundStatus = EnumCompetitorRoundStatus.CREATED;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public DBCompetitor getCompetitor() {
        return competitor;
    }

    public void setCompetitor(DBCompetitor competitor) {
        this.competitor = competitor;
    }

    public DBRound getRound() {
        return round;
    }

    public void setRound(DBRound round) {
        this.round = round;
    }

    public EnumHoldType getHoldType() {
        return holdType;
    }

    public void setHoldType(EnumHoldType holdType) {
        this.holdType = holdType;
    }

    public int getHoldNumber() {
        return holdNumber;
    }

    public void setHoldNumber(int holdNumber) {
        this.holdNumber = holdNumber;
    }

    public int getTryNumber() {
        return tryNumber;
    }

    public void setTryNumber(int tryNumber) {
        this.tryNumber = tryNumber;
    }

    public EnumCompetitorRoundStatus getCompetitorRoundStatus() {
        return competitorRoundStatus;
    }

    public void setCompetitorRoundStatus(EnumCompetitorRoundStatus competitorRoundStatus) {
        this.competitorRoundStatus = competitorRoundStatus;
    }
}
