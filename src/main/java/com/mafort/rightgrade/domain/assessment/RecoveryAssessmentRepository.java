package com.mafort.rightgrade.domain.assessment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RecoveryAssessmentRepository extends JpaRepository<RecoveryAssessment, UUID> {
    Optional<RecoveryAssessment> findByOriginalAssessmentId(UUID originalAssessmentId);
}
