package com.mafort.rightgrade.domain.assessment;

import com.mafort.rightgrade.domain.grade.Grade;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AssessmentResponse(
        UUID id,
        String name,
        UUID gradingPeriodId,
        LocalDateTime createdAt,
        double value,
        List<Grade> grades,
        boolean isRecovery,
        UUID originalAssessmentId,
        UUID recoveryAssessmentId)
{
    public AssessmentResponse(Assessment assessment){
        this(
                assessment.getId(),
                assessment.getName(),
                assessment.getGradingPeriod().getId(),
                assessment.getCreatedAt(),
                assessment.getValue(),
                assessment.getGrades(),
                false,
                null,
                null);
    }

    public AssessmentResponse(RecoveryAssessment recoveryAssessment){
        this(
                recoveryAssessment.getId(),
                recoveryAssessment.getName(),
                recoveryAssessment.getGradingPeriod().getId(),
                recoveryAssessment.getCreatedAt(),
                recoveryAssessment.getValue(),
                recoveryAssessment.getGrades(),
                true,
                recoveryAssessment.getOriginalAssessment().getId(),
                null);
    }
    public AssessmentResponse(Assessment assessment, UUID recoveryAssessmentId){
        this(
                assessment.getId(),
                assessment.getName(),
                assessment.getGradingPeriod().getId(),
                assessment.getCreatedAt(),
                assessment.getValue(),
                assessment.getGrades(),
                false,
                null,
                recoveryAssessmentId
        );
    }
}