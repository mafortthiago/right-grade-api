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
        List<Grade> grades)
{
    public AssessmentResponse(Assessment assessment){
        this(
                assessment.getId(),
                assessment.getName(),
                assessment.getGradingPeriod().getId(),
                assessment.getCreatedAt(),
                assessment.getValue(),
                assessment.getGrades());
    }
}
