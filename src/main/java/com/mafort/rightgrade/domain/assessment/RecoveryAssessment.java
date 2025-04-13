package com.mafort.rightgrade.domain.assessment;

import com.mafort.rightgrade.domain.grade.Grade;
import com.mafort.rightgrade.domain.gradingPeriod.GradingPeriod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecoveryAssessment implements AssessmentBase{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Setter
    private String name;
    @ManyToOne
    @JoinColumn(name = "grading_period_id")
    private GradingPeriod gradingPeriod;
    private LocalDateTime createdAt;
    @Setter
    private double value;
    @OneToMany(mappedBy = "recoveryAssessment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Grade> grades;

    @OneToOne
    @JoinColumn(name = "assessment_id")
    private Assessment originalAssessment;

    public RecoveryAssessment(Assessment originalAssessment) {
        this.name = originalAssessment.getName();
        this.value = originalAssessment.getValue();
        this.gradingPeriod = originalAssessment.getGradingPeriod();
        this.createdAt = LocalDateTime.now();
        this.originalAssessment = originalAssessment;
    }
}

