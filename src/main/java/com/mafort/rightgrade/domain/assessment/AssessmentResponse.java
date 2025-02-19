package com.mafort.rightgrade.domain.assessment;

import java.util.UUID;

public record AssessmentResponse(UUID id, String name, UUID gradingPeriodId) {
    public AssessmentResponse(Assessment assessment){
        this(assessment.getId(), assessment.getName(), assessment.getGradingPeriod().getId());
    }
}
