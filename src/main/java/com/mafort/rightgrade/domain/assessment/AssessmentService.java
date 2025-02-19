package com.mafort.rightgrade.domain.assessment;

import com.mafort.rightgrade.domain.gradingPeriod.GradingPeriod;
import com.mafort.rightgrade.domain.gradingPeriod.GradingPeriodRepository;
import com.mafort.rightgrade.domain.student.Student;
import com.mafort.rightgrade.infra.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AssessmentService {
    @Autowired
    private GradingPeriodRepository gradingPeriodRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;

    public Assessment create(CreateAssessment createAssessment){
        Optional<GradingPeriod> gradingPeriod = this.gradingPeriodRepository.findById(createAssessment.gradingPeriodId());
        if(gradingPeriod.isEmpty()){
            throw new NotFoundException("There is no grading period with the provided id");
        }
        Assessment assessment = new Assessment(createAssessment, gradingPeriod.get());
        this.assessmentRepository.save(assessment);
        return assessment;
    }
}
