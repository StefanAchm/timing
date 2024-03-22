package com.asi.timer.service;

import com.asi.timer.backend.utils.StartNumberUtil;
import com.asi.timer.model.db.DBCompetitor;
import com.asi.timer.model.view.CompetitorRequest;
import com.asi.timer.model.view.CompetitorResponse;
import com.asi.timer.model.view.RoundRequest;
import com.asi.timer.repositories.CompetitorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompetitorService {

    private final CompetitorRepository competitorRepository;

    public CompetitorService(CompetitorRepository competitorRepository) {
        this.competitorRepository = competitorRepository;
    }

    public DBCompetitor createCompetitor(CompetitorRequest competitorRequest) {

        DBCompetitor competitor = new DBCompetitor();
        competitor.setStartNumber(competitorRequest.getStartNumber());
        competitor.setFirstName(competitorRequest.getFirstName());
        competitor.setLastName(competitorRequest.getLastName());
        competitor.setCity(competitorRequest.getCity());
        competitor.setClub(competitorRequest.getClub());
        competitor.setDateOfBirth(competitorRequest.getDateOfBirth());
        competitor.setGender(competitorRequest.getGender());

        return this.competitorRepository.save(competitor);

    }

    public DBCompetitor updateCompetitor(CompetitorRequest competitorRequest) {

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

    public DBCompetitor deleteCompetitor(CompetitorRequest competitorRequest, boolean soft) {

        DBCompetitor competitor = this.competitorRepository
                .findById(competitorRequest.getId())
                .orElseThrow(() -> new RuntimeException("Competitor with id " + competitorRequest.getId() + " not found"));

        // TODO: attention, if the competitor is already in a round, this will not work!

        if(soft) {
            competitor.setDeleted(true);
            this.competitorRepository.save(competitor);
        } else {
            this.competitorRepository.delete(competitor);
        }

        return competitor;

    }

    public List<CompetitorResponse> getCompetitors() {

        return this.competitorRepository.findAllByDeletedFalse()
                .stream()
                .map(competitor -> CompetitorResponse.fromDBCompetitorRound(competitor)).toList();

    }

    public List<CompetitorResponse> getPossibleCompetitors(RoundRequest roundRequest) {

        return List.of(); // TODO: implement

    }

    public Integer generateStartNumber() {

        List<Integer> assignedStartNumbers = this.competitorRepository.findAllByDeletedFalse()
                .stream()
                .map(DBCompetitor::getStartNumber)
                .toList();

        return StartNumberUtil.getRandomStartNumber(assignedStartNumbers);

    }

}
