package com.asi.timer.repositories;

import com.asi.timer.model.db.Competitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompetitorRepository extends JpaRepository<Competitor, UUID> {
}
