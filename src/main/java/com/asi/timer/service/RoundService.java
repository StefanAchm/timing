package com.asi.timer.service;

import com.asi.timer.model.db.CompetitorRound;
import com.asi.timer.model.db.Round;
import com.asi.timer.model.view.RoundRequest;
import com.asi.timer.repositories.RoundRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoundService {

    private final RoundRepository roundRepository;

    public RoundService(RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    public Round createRound(RoundRequest roundRequest) {

        Round round = new Round();
        round.setId(UUID.randomUUID());
        round.setRoundNumber(roundRequest.getRoundNumber());
        round.setMaxHolds(roundRequest.getMaxHolds());
        round.setGender(roundRequest.getGender());

        return this.roundRepository.save(round);

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

}
