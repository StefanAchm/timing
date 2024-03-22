package com.asi.timer.service;

import com.asi.timer.model.db.DBRound;
import com.asi.timer.model.view.CreateRoundRequest;
import com.asi.timer.model.view.RoundRequest;
import com.asi.timer.model.view.RoundResponse;
import com.asi.timer.repositories.RoundRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoundService {

    private final RoundRepository roundRepository;

    private final CompetitorRoundService competitorRoundService;

    public RoundService(RoundRepository roundRepository, CompetitorRoundService competitorRoundService) {
        this.roundRepository = roundRepository;
        this.competitorRoundService = competitorRoundService;
    }

    public List<RoundResponse> getRounds() {

        return this.roundRepository.findAll().stream().map(round -> {

            RoundResponse roundResponse = new RoundResponse();
            roundResponse.setId(round.getId());
            roundResponse.setRoundNumber(round.getRoundNumber());
            roundResponse.setMaxHolds(round.getMaxHolds());
            roundResponse.setGender(round.getGender());

            roundResponse.setNumberOfCompetitors(round.getCompetitorRounds().size());

            return roundResponse;

        }).collect(Collectors.toList());

    }

    public DBRound createRound(CreateRoundRequest createRoundRequest, boolean addCompetitors) {

        DBRound round = new DBRound();

        round.setRoundNumber(createRoundRequest.getRoundRequest().getRoundNumber());
        round.setMaxHolds(createRoundRequest.getRoundRequest().getMaxHolds());
        round.setGender(createRoundRequest.getRoundRequest().getGender());

        DBRound roundCreated = this.roundRepository.save(round);

        // End transaction
//        this.roundRepository.flush(); // This is needed to get the ID of the round

        if (addCompetitors) {

            this.competitorRoundService.autoAddCompetitorsToRound(createRoundRequest.getScore(), roundCreated);

        }

        return roundCreated;

    }

    public DBRound updateRound(RoundRequest roundRequest) {

        DBRound round = this.roundRepository
                .findById(roundRequest.getId())
                .orElseThrow(() -> new RuntimeException("Round with id " + roundRequest.getId() + " not found"));

        round.setRoundNumber(roundRequest.getRoundNumber());
        round.setMaxHolds(roundRequest.getMaxHolds());
        round.setGender(roundRequest.getGender());

        // TODO: attention

        this.roundRepository.save(round);

        return round;

    }

    public DBRound deleteRound(RoundRequest roundRequest) {

        DBRound round = this.roundRepository
                .findById(roundRequest.getId())
                .orElseThrow(() -> new RuntimeException("Round with id " + roundRequest.getId() + " not found"));

        // TODO: attention

        this.roundRepository.delete(round);

        return round;

    }

    public RoundResponse preview(CreateRoundRequest createRoundRequest) {

        RoundResponse roundResponse = new RoundResponse();
        roundResponse.setRoundNumber(createRoundRequest.getRoundRequest().getRoundNumber());
        roundResponse.setMaxHolds(createRoundRequest.getRoundRequest().getMaxHolds());
        roundResponse.setGender(createRoundRequest.getRoundRequest().getGender());
        roundResponse.setSuccessScore(0);

        // TODO
//        roundResponse.setNumberOfCompetitors(createRoundRequest.getScore().getCompetitors().size());

        return roundResponse;

    }

}
