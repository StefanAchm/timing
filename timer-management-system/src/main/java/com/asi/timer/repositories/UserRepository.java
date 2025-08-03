package com.asi.timer.repositories;

import com.asi.timer.model.db.DBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<DBUser, UUID> {
    Optional<DBUser> findByUsername(String username);
}

