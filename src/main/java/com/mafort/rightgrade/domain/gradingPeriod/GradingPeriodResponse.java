package com.mafort.rightgrade.domain.gradingPeriod;

import com.mafort.rightgrade.domain.assessment.AssessmentResponse;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public record GradingPeriodResponse(UUID id, String name, List<AssessmentResponse> assessments, UUID groupId) {
    public GradingPeriodResponse(GradingPeriod gradingPeriod){
        this(gradingPeriod.getId(), gradingPeriod.getName(),
                gradingPeriod.getAssessments() != null
                        ?
                gradingPeriod.getAssessments().stream().map(AssessmentResponse::new).toList()
                        :
                Collections.emptyList(),
                gradingPeriod.getGroup().getId()
        );
    }
}
