package com.asi.timer.service;

import com.asi.timer.model.view.APIStatistics;
import com.asi.timer.repositories.CompetitorRepository;
import com.asi.timer.repositories.CompetitorRoundRepository;
import com.asi.timer.repositories.RoundRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final CompetitorRoundRepository competitorRoundRepository;

    private final RoundRepository roundRepository;

    private final CompetitorRepository competitorRepository;

    public AdminService(CompetitorRoundRepository competitorRoundRepository,
                           RoundRepository roundRepository,
                           CompetitorRepository competitorRepository) {
        this.competitorRoundRepository = competitorRoundRepository;
        this.roundRepository = roundRepository;
        this.competitorRepository = competitorRepository;
    }


    public void deleteAllData() {
        this.competitorRoundRepository.deleteAll();
        this.roundRepository.deleteAll();
        this.competitorRepository.deleteAll();
    }


    public APIStatistics getStatistics() {

        long competitorCount = this.competitorRepository.count();
        long roundCount = this.roundRepository.count();
        long competitorRoundCount = this.competitorRoundRepository.count();

        return new APIStatistics(competitorCount, roundCount, competitorRoundCount);

    }

}
