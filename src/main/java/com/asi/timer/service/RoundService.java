package com.asi.timer.service;

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
}
