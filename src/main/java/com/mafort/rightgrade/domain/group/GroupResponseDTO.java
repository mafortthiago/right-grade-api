package com.mafort.rightgrade.domain.group;

import com.mafort.rightgrade.domain.gradingPeriod.GradingPeriodResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public record GroupResponseDTO(UUID id, String name, LocalDateTime createdAt, boolean isGradeFrom0To100, List<GradingPeriodResponse> gradingPeriods) {
    public  GroupResponseDTO(Group group){
        this(
                group.getId(),
                group.getName(),
                group.getCreatedAt(),
                group.isGradeFrom0To100(),
                group.getGradingPeriods() != null
                        ?
                        group.getGradingPeriods().stream().map(GradingPeriodResponse::new).toList()
                        :
                        Collections.emptyList()
        );
    }
}
