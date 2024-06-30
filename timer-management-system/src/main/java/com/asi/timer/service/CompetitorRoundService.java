package com.asi.timer.service;

import com.asi.timer.backend.model.CompetitorRound;
import com.asi.timer.backend.model.Round;
import com.asi.timer.backend.utils.ScoreUtil;
import com.asi.timer.enums.EnumCompetitorRoundStatus;
import com.asi.timer.enums.EnumHoldType;
import com.asi.timer.model.db.DBCompetitor;
import com.asi.timer.model.db.DBCompetitorRound;
import com.asi.timer.model.db.DBRound;
import com.asi.timer.model.view.APICompetitor;
import com.asi.timer.model.view.APICompetitorRound;
import com.asi.timer.repositories.CompetitorRepository;
import com.asi.timer.repositories.CompetitorRoundRepository;
import com.asi.timer.repositories.RoundRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    private String add(DBCompetitor competitor, DBRound round) {

        DBCompetitorRound competitorRound = new DBCompetitorRound();
        competitorRound.setCompetitor(competitor);
        competitorRound.setRound(round);

        // make sure that all previous rounds are completed

        this.competitorRoundRepository
                .findAllByCompetitorId(competitor.getId())
                .stream()
                .filter(competitorRound1 -> competitorRound1.getRound().getRoundNumber() < round.getRoundNumber())
                .filter(competitorRound1 -> competitorRound1.getCompetitorRoundStatus() == EnumCompetitorRoundStatus.CREATED)
                .findFirst()
                .ifPresent(c -> {
                    throw new RuntimeException("Previous rounds are not completed");
                });

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

    /**
     * Add all possible competitors to the round
     */
    public void addCompetitorsToRound(List<DBCompetitor> competitors, DBRound dbRound) {

        competitors.forEach(competitor -> {

            DBCompetitorRound competitorRound = new DBCompetitorRound();
            competitorRound.setCompetitor(competitor);
            competitorRound.setRound(dbRound);

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

        return ScoreUtil.calculateScore(competitorRound.toBackendCompetitorRound());

    }

    public List<EnumHoldType> getHoldTypes() {

        return Arrays.stream(EnumHoldType.values()).toList();

    }

    public List<APICompetitorRound> getCompetitorRounds() {

        List<APICompetitorRound> competitorRoundsResponse = this.competitorRoundRepository.findAll()
                .stream()
                .map(competitorRound -> APICompetitorRound.fromDBCompetitorRound(competitorRound, true))
                .collect(Collectors.toList());

        // add competitors, which are not in a round yet

        this.competitorRepository
                .findAll()
                .stream()
                .filter(competitor -> competitor.getCompetitorRounds().isEmpty())
                .forEach(competitor -> {
                    APICompetitorRound competitorRound = new APICompetitorRound();
                    competitorRound.setCompetitor(APICompetitor.fromDBCompetitor(competitor, false));
                    competitorRoundsResponse.add(competitorRound);
                });

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Update all scores for all competitors:

        List<Round> rounds = this.roundRepository.findAll()
                .stream()
                .map(DBRound::toBackendRound)
                .toList();

        Map<UUID, Double> scores = new HashMap<>();
        for(DBCompetitor competitor : competitorRepository.findAll()) {

            List<CompetitorRound> competitorRounds = competitor.getCompetitorRounds()
                    .stream()
                    .map(DBCompetitorRound::toBackendCompetitorRound)
                    .toList();


            scores.put(competitor.getId(), ScoreUtil.calculateTotalScore(competitorRounds, rounds));

        }

        for(APICompetitorRound competitorRound : competitorRoundsResponse) {

            if(competitorRound.getCompetitor() != null) {
                competitorRound.getCompetitor().setTotalScore(scores.get(competitorRound.getCompetitor().getId()));
            }

        }

        return competitorRoundsResponse;

    }

    public String deleteCompetitorRound(UUID id) {

        DBCompetitorRound competitorRound = this.competitorRoundRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("CompetitorRound with id " + id + " not found"));

        // If there is a competitorRound, after this round, we can not delete it

        this.competitorRoundRepository
                .findAllByCompetitorId(competitorRound.getCompetitor().getId())
                .stream()
                .filter(competitorRound1 -> competitorRound1.getRound().getRoundNumber() > competitorRound.getRound().getRoundNumber())
                .findFirst()
                .ifPresent(c -> {
                    throw new RuntimeException("CompetitorRound with id " + id + " can not be deleted, because there is a round after this round");
                });


        this.competitorRoundRepository.delete(competitorRound);

        return "CompetitorRound with id " + id + " deleted";

    }

}
