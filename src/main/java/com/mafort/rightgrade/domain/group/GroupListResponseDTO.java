package com.mafort.rightgrade.domain.group;

import com.mafort.rightgrade.domain.gradingPeriod.GradingPeriodResponse;

import java.util.List;
import java.util.UUID;

public record GroupListResponseDTO(
        UUID id,
        String name,
        double minimumGrade,
        boolean isGradeFrom0To100,
        List<GradingPeriodResponse> gradingPeriods
) {
}
