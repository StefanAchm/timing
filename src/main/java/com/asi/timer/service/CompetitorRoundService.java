package com.asi.timer.service;

import com.asi.timer.backend.score.ScoreCalculator;
import com.asi.timer.model.db.Competitor;
import com.asi.timer.model.db.CompetitorRound;
import com.asi.timer.model.db.Round;
import com.asi.timer.model.view.CompetitorResponse;
import com.asi.timer.model.view.CompetitorRoundScoreRequest;
import com.asi.timer.repositories.CompetitorRepository;
import com.asi.timer.repositories.CompetitorRoundRepository;
import com.asi.timer.repositories.RoundRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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

        Competitor competitor = this.competitorRepository
                .findById(competitorId)
                .orElseThrow(() -> new RuntimeException("Competitor with id " + competitorId + " not found"));


        Round round = this.roundRepository
                .findById(roundId)
                .orElseThrow(() -> new RuntimeException("Round with id " + roundId + " not found"));

        CompetitorRound competitorRound = new CompetitorRound();
        competitorRound.setCompetitor(competitor);
        competitorRound.setRound(round);

        return this.competitorRoundRepository.save(competitorRound).getId().toString();
        
    }

    public double updateScore(UUID competitorRoundID, CompetitorRoundScoreRequest competitorRoundScoreRequest) {

        CompetitorRound competitorRound = this.competitorRoundRepository
                .findById(competitorRoundID)
                .orElseThrow(() -> new RuntimeException("CompetitorRound with id " + competitorRoundID + " not found"));

        competitorRound.setHoldNumber(competitorRoundScoreRequest.getHoldNumber());
        competitorRound.setHoldType(competitorRoundScoreRequest.getHoldType());
        competitorRound.setTryNumber(competitorRoundScoreRequest.getTryNumber());

        this.competitorRoundRepository.save(competitorRound);

        return ScoreCalculator.calculateScore(competitorRound);

    }

    public List<CompetitorResponse> getCompetitors(int roundNumber, String gender) {

        List<CompetitorRound> competitorRounds = this.competitorRoundRepository
                .findByRound_RoundNumberAndRound_Gender(roundNumber, gender);

        return competitorRounds.stream().map(competitorRound -> {

            CompetitorResponse competitorResponse = new CompetitorResponse();
            competitorResponse.setId(competitorRound.getCompetitor().getId());
            competitorResponse.setFirstName(competitorRound.getCompetitor().getFirstName());
            competitorResponse.setLastName(competitorRound.getCompetitor().getLastName());
            competitorResponse.setStartNumber(competitorRound.getCompetitor().getStartNumber());
            competitorResponse.setClub(competitorRound.getCompetitor().getClub());
            competitorResponse.setDomicil(competitorRound.getCompetitor().getCity());
            competitorResponse.setDateOfBirth(competitorRound.getCompetitor().getDateOfBirth());
            competitorResponse.setGender(competitorRound.getCompetitor().getGender());

            return competitorResponse;

        }).toList();

    }

    public void addCompetitorsToRound(Round newRound) {

        // 1. Find all possible candidates for the round

        Round previousRound = this.roundRepository.findByRoundNumberAndGender(newRound.getRoundNumber() - 1, newRound.getGender());

        List<Competitor> competitors = findPossibleCandidatesForRound(newRound, previousRound);

        // 2. Add all to the round

        competitors.forEach(competitor -> {

            CompetitorRound competitorRound = new CompetitorRound();
            competitorRound.setCompetitor(competitor);
            competitorRound.setRound(newRound);

            this.competitorRoundRepository.save(competitorRound);

        });

    }

    private List<Competitor> findPossibleCandidatesForRound(Round newRound, Round previousRound) {

        if(newRound.getRoundNumber() == 1) {
            return this.competitorRepository.findAllByGender(newRound.getGender());
        } else {

            throw new RuntimeException("Not implemented yet");

//            List<CompetitorRound> competitorRounds = this
//                    .competitorRoundRepository
//                    .findByRoundIdAndScoreGreaterThan(previousRound.getId(), previousRound.getSuccessScore());
//
//            return competitorRounds.stream().map(CompetitorRound::getCompetitor).toList();
        }

    }


}
