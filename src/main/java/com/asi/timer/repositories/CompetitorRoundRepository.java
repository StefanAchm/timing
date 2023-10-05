package com.asi.timer.repositories;

import com.asi.timer.model.db.CompetitorRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompetitorRoundRepository extends JpaRepository<CompetitorRound, UUID> {

}
