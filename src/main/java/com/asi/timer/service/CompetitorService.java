package com.asi.timer.service;

import com.asi.timer.model.db.Competitor;
import com.asi.timer.model.view.CompetitorRequest;
import com.asi.timer.repositories.CompetitorRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompetitorService {

    private final CompetitorRepository competitorRepository;

    public CompetitorService(CompetitorRepository competitorRepository) {
        this.competitorRepository = competitorRepository;
    }

    public Competitor createCompetitor(CompetitorRequest competitorRequest) {

        Competitor competitor = new Competitor();
        competitor.setId(UUID.randomUUID());
        competitor.setStartNumber(competitorRequest.getStartNumber());
        competitor.setFirstName(competitorRequest.getFirstName());
        competitor.setLastName(competitorRequest.getLastName());
        competitor.setDomicil(competitorRequest.getDomicil());
        competitor.setClub(competitorRequest.getClub());
        competitor.setDateOfBirth(competitorRequest.getDateOfBirth());
        competitor.setGender(competitorRequest.getGender());

        return this.competitorRepository.save(competitor);

    }

}
