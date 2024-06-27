package com.asi.timer.repositories;

import com.asi.timer.enums.EnumGender;
import com.asi.timer.model.db.DBRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoundRepository extends JpaRepository<DBRound, UUID> {

    Optional<DBRound> findByRoundNumberAndGender(int roundNumber, EnumGender gender);

    List<DBRound> findByGender(EnumGender gender);

}
