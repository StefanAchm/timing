package com.asi.timer.service;

import com.asi.timer.backend.score.ScoreCalculator;
import com.asi.timer.enums.EnumGender;
import com.asi.timer.enums.EnumHoldType;
import com.asi.timer.model.db.DBCompetitor;
import com.asi.timer.model.db.DBCompetitorRound;
import com.asi.timer.model.db.DBRound;
import com.asi.timer.model.view.APICompetitor;
import com.asi.timer.model.view.APICompetitorRound;
import com.asi.timer.model.view.APIRound;
import com.asi.timer.model.view.APIScore;
import com.asi.timer.repositories.CompetitorRepository;
import com.asi.timer.repositories.CompetitorRoundRepository;
import com.asi.timer.repositories.RoundRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompetitorRoundService {

    private final CompetitorRepository competitorRepository;
    private final RoundRepository roundRepository;
    private final CompetitorRoundRepository competitorRoundRepository;
    private final CompetitorService competitorService;

    public CompetitorRoundService(CompetitorRepository competitorRepository,
                                  RoundRepository roundRepository,
                                  CompetitorRoundRepository competitorRoundRepository,
                                  CompetitorService competitorService
                                  ) {

        this.competitorRepository = competitorRepository;
        this.roundRepository = roundRepository;
        this.competitorRoundRepository = competitorRoundRepository;
        this.competitorService = competitorService;

    }

    public String addCompetitorToRound(UUID competitorId, UUID roundId) {

        DBCompetitor competitor = this.competitorRepository
                .findById(competitorId)
                .orElseThrow(() -> new RuntimeException("Competitor with id " + competitorId + " not found"));


        DBRound round = this.roundRepository
                .findById(roundId)
                .orElseThrow(() -> new RuntimeException("Round with id " + roundId + " not found"));

        return add(competitor, round);

    }

    private String add(DBCompetitor competitor, DBRound round) {

        DBCompetitorRound competitorRound = new DBCompetitorRound();
        competitorRound.setCompetitor(competitor);
        competitorRound.setRound(round);

        return this.competitorRoundRepository.save(competitorRound).getId().toString();

    }

    public String addCompetitorToRound(UUID competitorId, Integer roundNumber) {

        DBCompetitor competitor = this.competitorRepository
                .findById(competitorId)
                .orElseThrow(() -> new RuntimeException("Competitor with id " + competitorId + " not found"));

        DBRound round = this.roundRepository.findByRoundNumberAndGender(roundNumber, competitor.getGender())
                .orElseThrow(() -> new RuntimeException("Round with number " + roundNumber + " for gender " + competitor.getGender() + " not found"));

        return add(competitor, round);

    }

    public double updateScore(UUID competitorRoundID, APICompetitorRound competitorRoundRequest) {

        DBCompetitorRound competitorRound = this.competitorRoundRepository
                .findById(competitorRoundID)
                .orElseThrow(() -> new RuntimeException("CompetitorRound with id " + competitorRoundID + " not found"));

        competitorRound.setHoldNumber(competitorRoundRequest.getHoldNumber());
        competitorRound.setHoldType(competitorRoundRequest.getHoldType());
        competitorRound.setTryNumber(competitorRoundRequest.getTryNumber());

        this.competitorRoundRepository.save(competitorRound);

        return ScoreCalculator.calculateScore(competitorRound);

    }

    public List<APICompetitor> getCompetitors(int roundNumber, EnumGender gender) {

        List<DBCompetitorRound> competitorRounds = this.competitorRoundRepository
                .findByRound_RoundNumberAndRound_Gender(roundNumber, gender);

        return competitorRounds
                .stream()
                .map(competitorRound -> APICompetitor.fromDBCompetitor(competitorRound.getCompetitor(), true))
                .toList();

    }

    /**
     * Find possible candidates for the round and add them to the round
     * @param apiRound the round
     */
    public void autoAddCompetitorsToRound(APIRound apiRound) {

        // 1. Find all possible candidates for the round

        List<DBCompetitor> competitors = competitorService.findPossibleCandidatesForRound(apiRound);

        DBRound round = this.roundRepository
                .findById(apiRound.getId())
                .orElseThrow(() -> new RuntimeException("Round with id " + apiRound.getId() + " not found"));

        // 2. Add all to the round

        competitors.forEach(competitor -> {

            DBCompetitorRound competitorRound = new DBCompetitorRound();
            competitorRound.setCompetitor(competitor);
            competitorRound.setRound(round);

            this.competitorRoundRepository.save(competitorRound);

        });

    }



    public List<APICompetitorRound> getCompetitorRounds(UUID roundId) {

        List<DBCompetitorRound> competitorRounds = this.competitorRoundRepository.findByRound_Id(roundId);

        return competitorRounds
                .stream()
                .map(competitorRound -> APICompetitorRound.fromDBCompetitorRound(competitorRound, true))
                .toList();

    }

    public Double update(APICompetitorRound competitorRoundRequest) {

        DBCompetitorRound competitorRound = this.competitorRoundRepository
                .findById(competitorRoundRequest.getId())
                .orElseThrow(() -> new RuntimeException("CompetitorRound with id " + competitorRoundRequest.getId() + " not found"));

        competitorRound.setHoldNumber(competitorRoundRequest.getHoldNumber());
        competitorRound.setHoldType(competitorRoundRequest.getHoldType());
        competitorRound.setTryNumber(competitorRoundRequest.getTryNumber());
        competitorRound.setCompetitorRoundStatus(competitorRoundRequest.getCompetitorRoundStatus());

        this.competitorRoundRepository.save(competitorRound);

        return ScoreCalculator.calculateScore(competitorRound);

    }

    public List<EnumHoldType> getHoldTypes() {

        return Arrays.stream(EnumHoldType.values()).toList();

    }

}
