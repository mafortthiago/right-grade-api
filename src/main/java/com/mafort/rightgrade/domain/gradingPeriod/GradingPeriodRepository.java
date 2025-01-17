package com.mafort.rightgrade.domain.gradingPeriod;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface GradingPeriodRepository extends JpaRepository<GradingPeriod, UUID> {
    @Query("SELECT gp FROM GradingPeriod gp ORDER BY gp.createdAt")
    List<GradingPeriod> findAllOrderByCreatedAt();
}
