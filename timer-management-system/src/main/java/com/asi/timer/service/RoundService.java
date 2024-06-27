package com.asi.timer.service;

import com.asi.timer.model.db.DBCompetitor;
import com.asi.timer.model.db.DBCompetitorRound;
import com.asi.timer.model.db.DBRound;
import com.asi.timer.model.view.APIRound;
import com.asi.timer.repositories.CompetitorRoundRepository;
import com.asi.timer.repositories.RoundRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoundService {

    private final RoundRepository roundRepository;
    private final CompetitorRoundRepository competitorRoundRepository;

    private final CompetitorRoundService competitorRoundService;

    private final CompetitorService competitorService;

    public RoundService(RoundRepository roundRepository,
                        CompetitorRoundRepository competitorRoundRepository,
                        CompetitorRoundService competitorRoundService,
                        CompetitorService competitorService
    ) {

        this.roundRepository = roundRepository;
        this.competitorRoundRepository = competitorRoundRepository;

        this.competitorRoundService = competitorRoundService;
        this.competitorService = competitorService;

    }

    public List<APIRound> getRounds() {

        return this.roundRepository.findAll()
                .stream()
                .map(APIRound::fromDBRound)
                .collect(Collectors.toList());

    }

    public APIRound createRound(APIRound apiRound, boolean addCompetitors) {

        DBRound round = DBRound.fromAPIRound(apiRound);

        DBRound roundCreated = this.roundRepository.save(round);

        if (addCompetitors) {

            apiRound.setId(roundCreated.getId());

            List<DBCompetitor> possibleCandidatesForRound = competitorService.findPossibleCandidatesForRound(apiRound);

            this.competitorRoundService.addCompetitorsToRound(possibleCandidatesForRound, round);

            apiRound.setNumberOfCompetitors(possibleCandidatesForRound.size());

        }

        return apiRound;

    }

    public DBRound updateRound(APIRound apiRound) {

        DBRound round = this.roundRepository
                .findById(apiRound.getId())
                .orElseThrow(() -> new RuntimeException("Round with id " + apiRound.getId() + " not found"));

        round.setRoundNumber(apiRound.getRoundNumber());
        round.setMaxHolds(apiRound.getMaxHolds());
        round.setGender(apiRound.getGender());

        this.roundRepository.save(round);

        return round;

    }

    public DBRound deleteRound(UUID roundId) {

        DBRound round = this.roundRepository
                .findById(roundId)
                .orElseThrow(() -> new RuntimeException("Round with id " + roundId + " not found"));

        // TODO: attention
        this.competitorRoundRepository.deleteAll(round.getCompetitorRounds()); // TODO: attention
        this.roundRepository.delete(round);

        return round;

    }

    public APIRound preview(APIRound apiRound) {

        APIRound roundResponse = new APIRound();
        roundResponse.setRoundNumber(apiRound.getRoundNumber());
        roundResponse.setMaxHolds(apiRound.getMaxHolds());
        roundResponse.setGender(apiRound.getGender());
        roundResponse.setSuccessScore(0);

        List<DBCompetitor> possibleCandidatesForRound = competitorService.findPossibleCandidatesForRound(apiRound);

        roundResponse.setNumberOfCompetitors(possibleCandidatesForRound.size());

        return roundResponse;

    }

}
