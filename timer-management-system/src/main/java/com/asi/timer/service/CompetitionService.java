package com.asi.timer.service;

import com.asi.timer.backend.model.CompetitorRound;
import com.asi.timer.backend.model.CompetitorScore;
import com.asi.timer.backend.model.Round;
import com.asi.timer.backend.utils.ScoreUtil;
import com.asi.timer.enums.EnumGender;
import com.asi.timer.model.db.DBCompetition;
import com.asi.timer.model.db.DBCompetitorRound;
import com.asi.timer.model.db.DBRound;
import com.asi.timer.model.view.APICompetitorRound;
import com.asi.timer.model.view.APICompetitorScore;
import com.asi.timer.model.view.APIRound;
import com.asi.timer.repositories.CompetitionRepository;
import com.asi.timer.repositories.CompetitorRepository;
import com.asi.timer.repositories.CompetitorRoundRepository;
import com.asi.timer.repositories.RoundRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final RoundRepository roundRepository;
    private final CompetitorRepository competitorRepository;
    private final CompetitorRoundRepository competitorRoundRepository;
    private final CompetitorRoundService competitorRoundService;

    public CompetitionService(CompetitionRepository competitionRepository,
                              RoundRepository roundRepository,
                              CompetitorRepository competitorRepository,
                              CompetitorRoundRepository competitorRoundRepository,
                              CompetitorRoundService competitorRoundService) {
        this.competitionRepository = competitionRepository;
        this.roundRepository = roundRepository;
        this.competitorRepository = competitorRepository;
        this.competitorRoundRepository = competitorRoundRepository;
        this.competitorRoundService = competitorRoundService;
    }

    public void update(UUID roundId, UUID competitorRoundId) {

        DBCompetition dbCompetition = getDbCompetition();

        if(roundId == null) {
            dbCompetition.setRound(null);
        } else {
            dbCompetition.setRound(roundRepository.findById(roundId).orElse(null));
        }

        if(competitorRoundId == null) {
            dbCompetition.setCompetitorRound(null);
        } else {
            dbCompetition.setCompetitorRound(competitorRoundRepository.findById(competitorRoundId).orElse(null));
        }

        competitionRepository.save(dbCompetition);

    }

    public void updateCurrentRound(UUID roundId) {
        DBCompetition dbCompetition = getDbCompetition();
        dbCompetition.setRound(roundRepository.findById(roundId).orElse(null));
        competitionRepository.save(dbCompetition);
    }

    public void updateCurrentCompetitorRound(UUID competitorRoundId) {
        DBCompetition dbCompetition = getDbCompetition();

        if(competitorRoundId == null) {
            dbCompetition.setCompetitorRound(null);
        } else {
            dbCompetition.setCompetitorRound(competitorRoundRepository.findById(competitorRoundId).orElse(null));
        }

        competitionRepository.save(dbCompetition);
    }

    public List<APICompetitorRound> getCurrentCompetitorRounds() {

        DBRound round = getDbCompetition().getRound();

        return round == null ? List.of() : this.competitorRoundService.getCompetitorRounds(round.getId());

    }

    public APICompetitorRound getCurrentCompetitorRound() {
        DBCompetitorRound competitorRound = getDbCompetition().getCompetitorRound();
        return competitorRound == null ? null : APICompetitorRound.fromDBCompetitorRound(competitorRound, true);
    }

    public APIRound getCurrentRound() {
        DBRound round = getDbCompetition().getRound();
        return round == null ? null : APIRound.fromDBRound(round);
    }

    public List<APICompetitorRound> getLatestCompetitorRounds(EnumGender gender) {

        DBRound latestRoundOfGender = roundRepository.findByGender(gender)
                .stream()
                .max(Comparator.comparing(DBRound::getRoundNumber))
                .orElse(null);

        if (latestRoundOfGender == null) {
            return List.of();
        }

        return this.competitorRoundRepository.findByRound_IdAndCompetitor_Gender(latestRoundOfGender.getId(), gender)
                .stream()
                .map(c -> APICompetitorRound.fromDBCompetitorRound(c, true))
                .toList();



    }

    public List<APICompetitorScore> getResultList(EnumGender gender) {

        List<CompetitorRound> competitorRounds = this.competitorRoundRepository.findByCompetitor_Gender(gender)
                .stream()
                .map(DBCompetitorRound::toBackendCompetitorRound)
                .toList();

        List<Round> rounds = this.roundRepository.findByGender(gender)
                .stream()
                .map(DBRound::toBackendRound)
                .toList();

        List<CompetitorScore> competitorScores = ScoreUtil.getCompetitorScores(competitorRounds, rounds);

        return competitorScores
                .stream()
                .map(APICompetitorScore::fromCompetitorScores)
                .toList();

    }

    // At the moment, we only have one competition, so we can use a single repository.
    private DBCompetition getDbCompetition() {
        return competitionRepository.findAll().stream().findFirst().orElseGet(DBCompetition::new);
    }

}
