package com.asi.timer.service;

import com.asi.timer.model.db.DBRound;
import com.asi.timer.model.view.APIRound;
import com.asi.timer.repositories.RoundRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoundService {

    private final RoundRepository roundRepository;

    private final CompetitorRoundService competitorRoundService;

    public RoundService(RoundRepository roundRepository, CompetitorRoundService competitorRoundService) {
        this.roundRepository = roundRepository;
        this.competitorRoundService = competitorRoundService;
    }

    public List<APIRound> getRounds() {

        return this.roundRepository.findAll()
                .stream()
                .map(APIRound::fromDBRound)
                .collect(Collectors.toList());

    }

    public DBRound createRound(APIRound apiRound, boolean addCompetitors) {

        DBRound round = new DBRound();

        round.setRoundNumber(apiRound.getRoundNumber());
        round.setMaxHolds(apiRound.getMaxHolds());
        round.setGender(apiRound.getGender());

        DBRound roundCreated = this.roundRepository.save(round);

        // End transaction
//        this.roundRepository.flush(); // This is needed to get the ID of the round

        if (addCompetitors) {

            this.competitorRoundService.autoAddCompetitorsToRound(apiRound.getApiScore(), roundCreated);

        }

        return roundCreated;

    }

    public DBRound updateRound(APIRound apiRound) {

        DBRound round = this.roundRepository
                .findById(apiRound.getId())
                .orElseThrow(() -> new RuntimeException("Round with id " + apiRound.getId() + " not found"));

        round.setRoundNumber(apiRound.getRoundNumber());
        round.setMaxHolds(apiRound.getMaxHolds());
        round.setGender(apiRound.getGender());

        // TODO: attention

        this.roundRepository.save(round);

        return round;

    }

    public DBRound deleteRound(UUID roundId) {

        DBRound round = this.roundRepository
                .findById(roundId)
                .orElseThrow(() -> new RuntimeException("Round with id " + roundId + " not found"));

        // TODO: attention

        this.roundRepository.delete(round);

        return round;

    }

    public APIRound preview(APIRound apiRound) {

        APIRound roundResponse = new APIRound();
        roundResponse.setRoundNumber(apiRound.getRoundNumber());
        roundResponse.setMaxHolds(apiRound.getMaxHolds());
        roundResponse.setGender(apiRound.getGender());
        roundResponse.setSuccessScore(0);

        // TODO
//        roundResponse.setNumberOfCompetitors(apiRound.getScore().getCompetitors().size());

        return roundResponse;

    }

}
