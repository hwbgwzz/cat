package com.cat.sys.repository;

import com.cat.sys.entity.Attempts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttemptsRepository extends JpaRepository<Attempts, Long> {
    Optional<Attempts> findAttemptsByUserName(String userName);
}
