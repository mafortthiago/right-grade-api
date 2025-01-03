package com.mafort.rightgrade.domain.gradingPeriod;

import java.util.UUID;

public record GradingPeriodResponse(UUID id, String name) {
    public GradingPeriodResponse(GradingPeriod gradingPeriod){
        this(gradingPeriod.getId(), gradingPeriod.getName());
    }
}
