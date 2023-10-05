package com.asi.timer.service;

import com.asi.timer.model.db.Competitor;
import com.asi.timer.model.db.CompetitorRound;
import com.asi.timer.model.db.Round;
import com.asi.timer.repositories.CompetitorRepository;
import com.asi.timer.repositories.CompetitorRoundRepository;
import com.asi.timer.repositories.RoundRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompetitorRoundService {

    private final CompetitorRepository competitorRepository;
    private final RoundRepository roundRepository;
    private final CompetitorRoundRepository competitorRoundRepository;

    public CompetitorRoundService(CompetitorRepository competitorRepository,
                                  RoundRepository roundRepository,
                                  CompetitorRoundRepository competitorRoundRepository) {

        this.competitorRepository = competitorRepository;
        this.roundRepository = roundRepository;
        this.competitorRoundRepository = competitorRoundRepository;

    }

    public String addCompetitorToRound(UUID competitorId, UUID roundId) {

        Competitor competitor = this.competitorRepository.findById(competitorId).orElseThrow();
        Round round = this.roundRepository.findById(roundId).orElseThrow();

        CompetitorRound competitorRound = new CompetitorRound();
        competitorRound.setId(UUID.randomUUID());
        competitorRound.setCompetitor(competitor);
        competitorRound.setRound(round);

        return this.competitorRoundRepository.save(competitorRound).getId().toString();
        
    }

    public String updateScore(UUID competitorRoundID, int score) {

        CompetitorRound competitorRound = this.competitorRoundRepository.findById(competitorRoundID).orElseThrow();
        competitorRound.setScore(score);
        this.competitorRoundRepository.save(competitorRound);
        return "Score updated";

    }
}
