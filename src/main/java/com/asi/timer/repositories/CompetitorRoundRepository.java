package com.asi.timer.repositories;

import com.asi.timer.model.db.DBCompetitorRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompetitorRoundRepository extends JpaRepository<DBCompetitorRound, UUID> {

    List<DBCompetitorRound> findByRound_RoundNumber(int roundNumber);

    List<DBCompetitorRound> findByRound_RoundNumberAndRound_Gender(int roundNumber, String gender);

}
