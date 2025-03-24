package com.mafort.rightgrade.domain.assessment;

import com.mafort.rightgrade.domain.gradingPeriod.GradingPeriod;
import com.mafort.rightgrade.domain.gradingPeriod.GradingPeriodRepository;
import com.mafort.rightgrade.domain.student.Student;
import com.mafort.rightgrade.infra.exception.InvalidArgumentException;
import com.mafort.rightgrade.infra.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    @Autowired
    private MessageSource messageSource;

    public Assessment create(CreateAssessment createAssessment){
        GradingPeriod gradingPeriod = findGradingPeriod(createAssessment.gradingPeriodId());
        this.validateAssessmentSum(createAssessment.value(), createAssessment.gradingPeriodId());
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

    private void validateAssessmentSum(double value, UUID gradingPeriodId){
        List<Assessment> assessments = this.assessmentRepository.findByGradingPeriodId(gradingPeriodId);

        double sum = 0;
        if(!assessments.isEmpty()){
          sum = assessments.stream().mapToDouble(Assessment::getValue).sum();
        }

        boolean isFrom0To100 = assessments.get(0).getGradingPeriod().getGroup().isGradeFrom0To100();
        double maxValue = isFrom0To100 ? 100 : 10;
        double currentValue = value + sum;
        
        if(currentValue > maxValue){
            String errorMessage = messageSource.getMessage("error.invalidAssessmentValue", null, LocaleContextHolder.getLocale());
            throw new InvalidArgumentException(errorMessage);
        }
    }
}
