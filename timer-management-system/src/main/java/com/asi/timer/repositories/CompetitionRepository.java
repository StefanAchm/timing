package com.asi.timer.repositories;

import com.asi.timer.model.db.DBCompetition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompetitionRepository extends JpaRepository<DBCompetition, UUID> {

}
