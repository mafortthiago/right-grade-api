package com.mafort.rightgrade.domain.grade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mafort.rightgrade.domain.assessment.Assessment;
import com.mafort.rightgrade.domain.assessment.RecoveryAssessment;
import com.mafort.rightgrade.domain.student.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@NoArgsConstructor
@Getter
@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnore
    private Student student;
    @ManyToOne
    @JoinColumn(name = "assessment_id")
    @JsonIgnore
    private Assessment assessment;
    @Setter
    private double value;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "recovery_assessment_id")
    private RecoveryAssessment recoveryAssessment;

    public Grade(CreateGrade createGrade, Assessment assessment, Student student){
        this.assessment = assessment;
        this.student = student;
        this.value = createGrade.value();
    }

    public Grade(CreateGrade createGrade, RecoveryAssessment recoveryAssessment, Student student){
        this.recoveryAssessment = recoveryAssessment;
        this.student = student;
        this.value = createGrade.value();
    }
}
