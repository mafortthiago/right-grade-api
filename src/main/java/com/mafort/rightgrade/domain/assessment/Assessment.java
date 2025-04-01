package com.mafort.rightgrade.domain.assessment;

import com.mafort.rightgrade.domain.grade.Grade;
import com.mafort.rightgrade.domain.gradingPeriod.GradingPeriod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Assessment implements AssessmentBase{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "grading_period_id")
    private GradingPeriod gradingPeriod;
    private LocalDateTime createdAt;
    private double value;
    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Grade> grades;

    public Assessment(CreateAssessment createAssessment, GradingPeriod gradingPeriod){
        this.name = createAssessment.name();
        this.value = createAssessment.value();
        this.gradingPeriod = gradingPeriod;
        this.createdAt = LocalDateTime.now();
    }
}
