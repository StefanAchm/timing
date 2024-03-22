package com.asi.timer.service;

import com.asi.timer.backend.score.ScoreCalculator;
import com.asi.timer.backend.utils.StartNumberUtil;
import com.asi.timer.model.db.DBCompetitor;
import com.asi.timer.model.db.DBCompetitorRound;
import com.asi.timer.model.db.DBRound;
import com.asi.timer.model.view.APICompetitor;
import com.asi.timer.model.view.APIRound;
import com.asi.timer.model.view.APIScore;
import com.asi.timer.repositories.CompetitorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompetitorService {

    private final CompetitorRepository competitorRepository;

    public CompetitorService(CompetitorRepository competitorRepository) {
        this.competitorRepository = competitorRepository;
    }

    public DBCompetitor createCompetitor(APICompetitor competitorRequest) {

        DBCompetitor competitor = DBCompetitor.fromAPICompetitor(competitorRequest);

        return this.competitorRepository.save(competitor);

    }

    public DBCompetitor updateCompetitor(APICompetitor competitorRequest) {

        DBCompetitor competitor = this.competitorRepository
                .findById(competitorRequest.getId())
                .orElseThrow(() -> new RuntimeException("Competitor with id " + competitorRequest.getId() + " not found"));

        competitor.setStartNumber(competitorRequest.getStartNumber());
        competitor.setFirstName(competitorRequest.getFirstName());
        competitor.setLastName(competitorRequest.getLastName());
        competitor.setCity(competitorRequest.getCity());
        competitor.setClub(competitorRequest.getClub());
        competitor.setDateOfBirth(competitorRequest.getDateOfBirth());
        competitor.setGender(competitorRequest.getGender());

        // TODO: attention, if the competitor is already in a round, this will not work!

        this.competitorRepository.save(competitor);

        return competitor;

    }

    public DBCompetitor deleteCompetitor(UUID competitorId, boolean soft) {

        DBCompetitor competitor = this.competitorRepository
                .findById(competitorId)
                .orElseThrow(() -> new RuntimeException("Competitor with id " + competitorId + " not found"));

        // TODO: attention, if the competitor is already in a round, this will not work!

        if(soft) {
            competitor.setDeleted(true);
            this.competitorRepository.save(competitor);
        } else {
            this.competitorRepository.delete(competitor);
        }

        return competitor;

    }

    public List<APICompetitor> getCompetitors() {

        return this.competitorRepository.findAllByDeletedFalse()
                .stream()
                .map(competitor -> APICompetitor.fromDBCompetitor(competitor, true)).toList();

    }

    public List<APICompetitor> getPossibleCompetitors(APIRound roundRequest) {

        return List.of(); // TODO: implement

    }

    public Integer generateStartNumber() {

        List<Integer> assignedStartNumbers = this.competitorRepository.findAllByDeletedFalse()
                .stream()
                .map(DBCompetitor::getStartNumber)
                .toList();

        return StartNumberUtil.getRandomStartNumber(assignedStartNumbers);

    }

    public List<DBCompetitor> findPossibleCandidatesForRound(APIRound round) {

        if (round.getRoundNumber() == 1) {

            return this.competitorRepository.findAllByGenderAndDeletedFalse(round.getGender());

        } else {

            // Find all, where holdNumber >= holdNumber and holdType == holdType and tryNumber >= tryNumber

            double minimumScore = ScoreCalculator.calculateScore(
                    round.getScore().getHoldNumber(),
                    round.getScore().getHoldType(),
                    round.getScore().getTryNumber()
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

}
