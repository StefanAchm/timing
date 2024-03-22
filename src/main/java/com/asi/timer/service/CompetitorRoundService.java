package com.asi.timer.service;

import com.asi.timer.backend.score.ScoreCalculator;
import com.asi.timer.enums.EnumGender;
import com.asi.timer.enums.EnumHoldType;
import com.asi.timer.model.db.DBCompetitor;
import com.asi.timer.model.db.DBCompetitorRound;
import com.asi.timer.model.db.DBRound;
import com.asi.timer.model.view.APICompetitor;
import com.asi.timer.model.view.APICompetitorRound;
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

    public CompetitorRoundService(CompetitorRepository competitorRepository,
                                  RoundRepository roundRepository,
                                  CompetitorRoundRepository competitorRoundRepository) {

        this.competitorRepository = competitorRepository;
        this.roundRepository = roundRepository;
        this.competitorRoundRepository = competitorRoundRepository;

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
     *
     * @param apiScore minimum score
     * @param round    Round
     */
    public void autoAddCompetitorsToRound(APIScore apiScore, DBRound round) {

        // 1. Find all possible candidates for the round

        List<DBCompetitor> competitors = findPossibleCandidatesForRound(round, apiScore);

        // 2. Add all to the round

        competitors.forEach(competitor -> {

            DBCompetitorRound competitorRound = new DBCompetitorRound();
            competitorRound.setCompetitor(competitor);
            competitorRound.setRound(round);

            this.competitorRoundRepository.save(competitorRound);

        });

    }

    private List<DBCompetitor> findPossibleCandidatesForRound(DBRound round, APIScore apiScore) {

        if (round.getRoundNumber() == 1) {

            return this.competitorRepository.findAllByGenderAndDeletedFalse(round.getGender());

        } else {

            // Find all, where holdNumber >= holdNumber and holdType == holdType and tryNumber >= tryNumber

            double minimumScore = ScoreCalculator.calculateScore(
                    apiScore.getHoldNumber(),
                    apiScore.getHoldType(),
                    apiScore.getTryNumber()
            );

            List<DBCompetitor> allByGenderAndDeletedFalse = this.competitorRepository.findAllByGenderAndDeletedFalse(round.getGender());

            List<DBCompetitor> competitors = new ArrayList<>();

            for (DBCompetitor competitor : allByGenderAndDeletedFalse) {

                Optional<DBCompetitorRound> first = competitor.getCompetitorRounds()
                        .stream()
                        .filter(competitorRound1 -> competitorRound1.getRound().getRoundNumber() == round.getRoundNumber() - 1)
                        .findFirst();

                if (first.isPresent()) {

                    DBCompetitorRound competitorRound = first.get();

                    double roundScore = ScoreCalculator.calculateScore(competitorRound.getHoldNumber(),
                            competitorRound.getHoldType(),
                            competitorRound.getTryNumber());

                    if (roundScore >= minimumScore) {
                        competitors.add(competitor);
                    }


                }


            }

            return competitors;
        }

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
