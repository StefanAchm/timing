package com.asi.timer.service;

import com.asi.timer.backend.CsvUtil;
import com.asi.timer.backend.utils.ScoreUtil;
import com.asi.timer.backend.utils.StartNumberUtil;
import com.asi.timer.model.db.DBCompetitor;
import com.asi.timer.model.db.DBCompetitorRound;
import com.asi.timer.model.view.APICompetitor;
import com.asi.timer.model.view.APIRound;
import com.asi.timer.repositories.CompetitorRepository;
import com.asi.timer.repositories.CompetitorRoundRepository;
import com.asi.timer.repositories.RoundRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompetitorService {

    private final CompetitorRepository competitorRepository;
    private final CompetitorRoundRepository competitorRoundRepository;
    private final RoundRepository roundRepository;
    private final CompetitorRoundService competitorRoundService;

    public CompetitorService(CompetitorRepository competitorRepository,
                             CompetitorRoundRepository competitorRoundRepository,
                             RoundRepository roundRepository,
                             CompetitorRoundService competitorRoundService) {

        this.competitorRepository = competitorRepository;
        this.competitorRoundRepository = competitorRoundRepository;
        this.roundRepository = roundRepository;
        this.competitorRoundService = competitorRoundService;

    }

    public APICompetitor registerCompetitor(APICompetitor competitorRequest) {

        int maxRetries = 3;
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                Integer startNumber = generateStartNumber();
                competitorRequest.setStartNumber(startNumber);
                DBCompetitor competitor = createCompetitorInDB(competitorRequest);
                return APICompetitor.fromDBCompetitor(competitor, false);
            } catch (DataIntegrityViolationException e) {
                if (attempt == maxRetries - 1) {
                    throw e; // Rethrow the exception if max retries reached
                }
                // Small delay before retry
                try { Thread.sleep(50); } catch (InterruptedException ie) { }
            }
        }
        throw new RuntimeException("Failed to register competitor");

    }

    public APICompetitor createCompetitor(APICompetitor competitorRequest) {
        try {
            DBCompetitor competitor = createCompetitorInDB(competitorRequest);
            return APICompetitor.fromDBCompetitor(competitor, false);
        } catch (DataIntegrityViolationException e) {

            String message = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();

            if(message != null) {

                if (message.contains("uc_competitor_first_last_name")) {
                    throw new IllegalArgumentException("Competitor " + competitorRequest.getFirstName() + " " + competitorRequest.getLastName() + " already exists!", e);
                } else if (message.contains("uc_competitor_start_number")) {
                    throw new IllegalArgumentException("Start number " + competitorRequest.getStartNumber() + " is already taken", e);
                }

            }

            throw new RuntimeException("Error creating competitor: " + e.getMessage(), e);

        }

    }

    private DBCompetitor createCompetitorInDB(APICompetitor competitorRequest) {

        DBCompetitor competitor = DBCompetitor.fromAPICompetitor(competitorRequest);

        DBCompetitor save = this.competitorRepository.save(competitor);

        this.competitorRoundService.addCompetitorToFirstRoundIfExists(save);

        return save;

    }

    public DBCompetitor updateCompetitor(APICompetitor competitorRequest) {

        DBCompetitor competitor = this.competitorRepository
                .findById(competitorRequest.getId())
                .orElseThrow(() -> new RuntimeException("Competitor with id " + competitorRequest.getId() + " not found"));

        if (!isStartNumberValid(competitor.getId(), competitorRequest.getStartNumber())) {
            throw new RuntimeException("Start number already in use");
        }

        competitor.setStartNumber(competitorRequest.getStartNumber());
        competitor.setFirstName(competitorRequest.getFirstName());
        competitor.setLastName(competitorRequest.getLastName());
        competitor.setCity(competitorRequest.getCity());
        competitor.setClub(competitorRequest.getClub());
        competitor.setDateOfBirth(competitorRequest.getDateOfBirth());
        competitor.setPaymentStatus(competitorRequest.getPaymentStatus());

        boolean genderChanged = !competitor.getGender().equals(competitorRequest.getGender());
        competitor.setGender(competitorRequest.getGender());


        if(!genderChanged) {

            this.competitorRepository.save(competitor);

        } else {

            // Each competitorround is mapped to a round, which is mapped to a gender
            // if the gender of a competitor changes, the competitorrounds relation has to be updated to the correct round
            // this is done by updating the round of the competitorround to the round with the same number but the correct gender

            List<DBCompetitorRound> competitorRounds = this.competitorRoundRepository.findAllByCompetitorId(competitor.getId());

            // Dry run:
            for (DBCompetitorRound competitorRound : competitorRounds) {

                this.roundRepository.findByRoundNumberAndGender(
                        competitorRound.getRound().getRoundNumber(),
                        competitorRequest.getGender()
                ).orElseThrow(() -> new RuntimeException("Round not found"));

            }

            // Now, we can update the competitor
            this.competitorRepository.save(competitor);

            // Actual run:
            for (DBCompetitorRound competitorRound : competitorRounds) {

                this.roundRepository.findByRoundNumberAndGender(
                        competitorRound.getRound().getRoundNumber(),
                        competitorRequest.getGender()
                ).ifPresentOrElse(
                        competitorRound::setRound,
                        () -> {throw new RuntimeException("Round not found");}
                );

                this.competitorRoundRepository.save(competitorRound);

            }


        }

        return competitor;

    }

    public DBCompetitor deleteCompetitor(UUID competitorId) {

        DBCompetitor competitor = this.competitorRepository
                .findById(competitorId)
                .orElseThrow(() -> new RuntimeException("Competitor with id " + competitorId + " not found"));

        this.competitorRepository.delete(competitor);

        return competitor;

    }

    public List<APICompetitor> getCompetitors() {

        return this.competitorRepository.findAll()
                .stream()
                .map(competitor -> APICompetitor.fromDBCompetitor(competitor, true)).toList();

    }

    public Integer generateStartNumber() {

        List<Integer> assignedStartNumbers = this.competitorRepository.findAll()
                .stream()
                .map(DBCompetitor::getStartNumber)
                .toList();

        return StartNumberUtil.getRandomStartNumber(assignedStartNumbers);

    }

    public boolean isStartNumberValid(UUID id, Integer startNumber) {

        List<Integer> assignedStartNumbers = this.competitorRepository.findAll()
                .stream()
                .filter(competitor -> !competitor.getId().equals(id))
                .map(DBCompetitor::getStartNumber)
                .toList();

        return !assignedStartNumbers.contains(startNumber);

    }

    public List<DBCompetitor> findPossibleCandidatesForRound(APIRound round) {

        if (round.getRoundNumber() == 1) {

            return this.competitorRepository.findAllByGender(round.getGender());

        } else {

            // Find all, where holdNumber >= holdNumber and holdType == holdType and tryNumber >= tryNumber

            double minimumScore = ScoreUtil.getPoints(
                    round.getScore().getHoldNumber(),
                    round.getScore().getHoldType(),
                    round.getScore().getTryNumber()
            );

            List<DBCompetitor> allByGenderAndDeletedFalse = this.competitorRepository.findAllByGender(round.getGender());

            List<DBCompetitor> competitors = new ArrayList<>();

            for (DBCompetitor competitor : allByGenderAndDeletedFalse) {

                Optional<DBCompetitorRound> first = competitor.getCompetitorRounds()
                        .stream()
                        .filter(competitorRound1 -> competitorRound1.getRound().getRoundNumber() == round.getRoundNumber() - 1)
                        .findFirst();

                if (first.isPresent()) {

                    DBCompetitorRound competitorRound = first.get();

                    double roundScore = ScoreUtil.getPoints(
                            competitorRound.getHoldNumber(),
                            competitorRound.getHoldType(),
                            competitorRound.getTryNumber()
                    );

                    if (roundScore >= minimumScore) {
                        competitors.add(competitor);
                    }


                }


            }

            return competitors;
        }

    }


    public List<APICompetitor> getPossibleCompetitors() {

        return CsvUtil.parseFromFile("possibleCompetitors.csv");

    }

}
