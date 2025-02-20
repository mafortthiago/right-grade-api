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
        GradingPeriod gradingPeriod = findGradingPeriod(createAssessment.gradingPeriodId());
        Assessment assessment = new Assessment(createAssessment, gradingPeriod);
        this.assessmentRepository.save(assessment);
        return assessment;
    }

    public List<AssessmentResponse> getByGradingPeriodId(UUID gradingPeriodId){
        GradingPeriod gradingPeriod = findGradingPeriod(gradingPeriodId);
        List<Assessment> assessments = this.assessmentRepository.findByGradingPeriodId(gradingPeriodId);
        return this.convertToAssessmentResponse(assessments);
    }

    private GradingPeriod findGradingPeriod(UUID gradingPeriodId){
        Optional<GradingPeriod> gradingPeriod = this.gradingPeriodRepository.findById(gradingPeriodId);
        if(gradingPeriod.isEmpty()){
            throw new NotFoundException("There is no grading period with the provided id");
        }
        return gradingPeriod.get();
    }

    private List<AssessmentResponse> convertToAssessmentResponse(List<Assessment> assessments){
        return assessments.stream().map(a -> new AssessmentResponse(a)).toList();
    }
}
