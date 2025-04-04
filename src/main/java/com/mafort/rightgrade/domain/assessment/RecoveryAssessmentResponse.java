package com.mafort.rightgrade.domain.assessment;

import com.mafort.rightgrade.domain.grade.Grade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record RecoveryAssessmentResponse(
        UUID id,
        String name,
        UUID gradingPeriodId,
        LocalDateTime createdAt,
        double value,
        List<Grade> grades,
        UUID originalAssessmentId)
{
    public RecoveryAssessmentResponse(RecoveryAssessment recoveryAssessment){
        this(
                recoveryAssessment.getId(),
                recoveryAssessment.getName(),
                recoveryAssessment.getGradingPeriod().getId(),
                recoveryAssessment.getCreatedAt(),
                recoveryAssessment.getValue(),
                recoveryAssessment.getGrades(),
                recoveryAssessment.getOriginalAssessment().getId());
    }
}