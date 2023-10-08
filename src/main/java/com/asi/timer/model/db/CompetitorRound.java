package com.asi.timer.model.db;

import com.asi.timer.enums.EnumHoldType;

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

    private EnumHoldType holdType;

    private int holdNumber;

    private int tryNumber;

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

}
