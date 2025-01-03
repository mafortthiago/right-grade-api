package com.mafort.rightgrade.domain.gradingPeriod;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GradingPeriodRepository extends JpaRepository<GradingPeriod, UUID> {
}
