package com.mafort.rightgrade.domain.assessment;

import com.mafort.rightgrade.domain.student.StudentListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {
    List<Assessment> findByGradingPeriodId(UUID gradingPeriodId);
}
