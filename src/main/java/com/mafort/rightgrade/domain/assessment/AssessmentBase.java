package com.mafort.rightgrade.domain.assessment;

import com.mafort.rightgrade.domain.grade.Grade;
import com.mafort.rightgrade.domain.gradingPeriod.GradingPeriod;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AssessmentBase {
    UUID getId();
    String getName();
    GradingPeriod getGradingPeriod();
    LocalDateTime getCreatedAt();
    double getValue();
    List<Grade> getGrades();
}
