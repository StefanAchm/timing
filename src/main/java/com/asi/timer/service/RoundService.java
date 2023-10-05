package com.asi.timer.service;

import com.asi.timer.model.db.CompetitorRound;
import com.asi.timer.model.db.Round;
import com.asi.timer.model.view.RoundRequest;
import com.asi.timer.repositories.CompetitorRoundRepository;
import com.asi.timer.repositories.RoundRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoundService {

    private final RoundRepository roundRepository;

    private final CompetitorRoundService competitorRoundService;

    public RoundService(RoundRepository roundRepository, CompetitorRoundService competitorRoundService) {
        this.roundRepository = roundRepository;
        this.competitorRoundService = competitorRoundService;
    }

    public Round createRound(RoundRequest roundRequest, boolean addCompetitors) {

        Round round = new Round();
        round.setRoundNumber(roundRequest.getRoundNumber());
        round.setMaxHolds(roundRequest.getMaxHolds());
        round.setGender(roundRequest.getGender());

        Round roundCreated = this.roundRepository.save(round);

        // End transaction
//        this.roundRepository.flush(); // This is needed to get the ID of the round

        if (addCompetitors) {
            this.competitorRoundService.addCompetitorsToRound(roundCreated);
        }

        return roundCreated;

    }

    public Round updateRound(RoundRequest roundRequest) {

        Round round = this.roundRepository
                .findById(roundRequest.getId())
                .orElseThrow(() -> new RuntimeException("Round with id " + roundRequest.getId() + " not found"));

        round.setRoundNumber(roundRequest.getRoundNumber());
        round.setMaxHolds(roundRequest.getMaxHolds());
        round.setGender(roundRequest.getGender());

        // TODO: attention

        this.roundRepository.save(round);

        return round;

    }

    public Round deleteRound(RoundRequest roundRequest) {

        Round round = this.roundRepository
                .findById(roundRequest.getId())
                .orElseThrow(() -> new RuntimeException("Round with id " + roundRequest.getId() + " not found"));

        // TODO: attention

        this.roundRepository.delete(round);

        return round;

    }

    public Round updateSuccessScore(UUID roundId, int successScore) {

        Round round = this.roundRepository
                .findById(roundId)
                .orElseThrow(() -> new RuntimeException("Round with id " + roundId + " not found"));

        round.setSuccessScore(successScore);

        return this.roundRepository.save(round);

    }
}
