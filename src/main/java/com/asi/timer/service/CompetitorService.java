package com.asi.timer.service;

import com.asi.timer.model.db.Competitor;
import com.asi.timer.model.view.CompetitorRequest;
import com.asi.timer.repositories.CompetitorRepository;
import org.springframework.stereotype.Service;

@Service
public class CompetitorService {

    private final CompetitorRepository competitorRepository;

    public CompetitorService(CompetitorRepository competitorRepository) {
        this.competitorRepository = competitorRepository;
    }

    public Competitor createCompetitor(CompetitorRequest competitorRequest) {

        Competitor competitor = new Competitor();
        competitor.setStartNumber(competitorRequest.getStartNumber());
        competitor.setFirstName(competitorRequest.getFirstName());
        competitor.setLastName(competitorRequest.getLastName());
        competitor.setCity(competitorRequest.getDomicil());
        competitor.setClub(competitorRequest.getClub());
        competitor.setDateOfBirth(competitorRequest.getDateOfBirth());
        competitor.setGender(competitorRequest.getGender());

        return this.competitorRepository.save(competitor);

    }

    public Competitor updateCompetitor(CompetitorRequest competitorRequest) {

        Competitor competitor = this.competitorRepository
                .findById(competitorRequest.getId())
                .orElseThrow(() -> new RuntimeException("Competitor with id " + competitorRequest.getId() + " not found"));

        competitor.setStartNumber(competitorRequest.getStartNumber());
        competitor.setFirstName(competitorRequest.getFirstName());
        competitor.setLastName(competitorRequest.getLastName());
        competitor.setCity(competitorRequest.getDomicil());
        competitor.setClub(competitorRequest.getClub());
        competitor.setDateOfBirth(competitorRequest.getDateOfBirth());
        competitor.setGender(competitorRequest.getGender());

        // TODO: attention, if the competitor is already in a round, this will not work!

        this.competitorRepository.save(competitor);

        return competitor;

    }

    public Competitor deleteCompetitor(CompetitorRequest competitorRequest) {

        Competitor competitor = this.competitorRepository
                .findById(competitorRequest.getId())
                .orElseThrow(() -> new RuntimeException("Competitor with id " + competitorRequest.getId() + " not found"));

        // TODO: attention, if the competitor is already in a round, this will not work!

        this.competitorRepository.delete(competitor);

        return competitor;

    }
}
