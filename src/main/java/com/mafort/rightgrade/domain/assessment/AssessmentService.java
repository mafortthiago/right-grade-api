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

import java.util.*;

@Service
public class AssessmentService {
    @Autowired
    private GradingPeriodRepository gradingPeriodRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private RecoveryAssessmentRepository recoveryAssessmentRepository;
    @Autowired
    private MessageSource messageSource;

    public Assessment create(CreateAssessment createAssessment){
        GradingPeriod gradingPeriod = findGradingPeriod(createAssessment.gradingPeriodId());
        if(!createAssessment.isRecovery()){
            this.validateAssessmentSum(createAssessment.value(), createAssessment.gradingPeriodId());
        }
        Assessment assessment = new Assessment(createAssessment, gradingPeriod);
        this.assessmentRepository.save(assessment);
        return assessment;
    }

    public List<AssessmentResponse> getByGradingPeriodId(UUID gradingPeriodId) {
        GradingPeriod gradingPeriod = findGradingPeriod(gradingPeriodId);
        List<Assessment> assessments = this.assessmentRepository.findByGradingPeriodId(gradingPeriodId);
        var sortedAssessments = this.sortAssessments(assessments);
        return this.convertToAssessmentResponse(sortedAssessments);
    }

    private List<AssessmentBase> sortAssessments(List<Assessment> assessments){
        List<AssessmentBase> assessmentBases= new ArrayList<>();
        for(Assessment assessment : assessments){
            assessmentBases.add(assessment);
            Optional<RecoveryAssessment> recoveryAssessmentOptional = this.findRecoveryAssessment(assessment.getId());
            recoveryAssessmentOptional.ifPresent(assessmentBases::add);
        }
        return assessmentBases;
    }

    private Optional<RecoveryAssessment> findRecoveryAssessment(UUID originalAssessmentId){
        return this.recoveryAssessmentRepository.findByOriginalAssessmentId(originalAssessmentId);
    }

    private GradingPeriod findGradingPeriod(UUID gradingPeriodId){
        Optional<GradingPeriod> gradingPeriod = this.gradingPeriodRepository.findById(gradingPeriodId);
        if(gradingPeriod.isEmpty()){
            throw new NotFoundException("There is no grading period with the provided id");
        }
        return gradingPeriod.get();
    }

    private List<AssessmentResponse> convertToAssessmentResponse(List<AssessmentBase> assessments) {
        List<AssessmentResponse> responses = new ArrayList<>();
        Map<UUID, UUID> recoveryIdMap = new HashMap<>();

        for (AssessmentBase a : assessments) {
            if (a instanceof RecoveryAssessment recovery) {
                recoveryIdMap.put(recovery.getOriginalAssessment().getId(), recovery.getId());
            }
        }

        for (AssessmentBase assessment : assessments) {
            if (assessment instanceof Assessment regular) {
                UUID recoveryId = recoveryIdMap.get(regular.getId());
                responses.add(new AssessmentResponse(regular, recoveryId));
            } else if (assessment instanceof RecoveryAssessment recovery) {
                responses.add(new AssessmentResponse(recovery));
            }
        }
        return responses;
    }

    private void validateAssessmentSum(double value, UUID gradingPeriodId) {
        List<Assessment> assessments = this.assessmentRepository.findByGradingPeriodId(gradingPeriodId);

        double sum = 0;
        if (!assessments.isEmpty()) {
            sum = assessments.stream().mapToDouble(Assessment::getValue).sum();
        }

        boolean isFrom0To100 = true;
        if (!assessments.isEmpty()) {
            isFrom0To100 = assessments.get(0).getGradingPeriod().getGroup().isGradeFrom0To100();
        }
        double maxValue = isFrom0To100 ? 100 : 10;
        double currentValue = value + sum;

        if (currentValue > maxValue) {
            String errorMessage = messageSource.getMessage("error.invalidAssessmentValue", null, LocaleContextHolder.getLocale());
            throw new InvalidArgumentException(errorMessage);
        }
    }

    public RecoveryAssessmentResponse createRecovery(CreateRecoveryAssessment request){
        Assessment assessment = this.findAssessment(request.assessmentId());
        RecoveryAssessment recoveryAssessment = new RecoveryAssessment(assessment);
        recoveryAssessment.setName(
                recoveryAssessment.getName() + " " +
                        this.messageSource.getMessage("recovery",null, LocaleContextHolder.getLocale()));
        this.recoveryAssessmentRepository.save(recoveryAssessment);
        return new RecoveryAssessmentResponse(recoveryAssessment);
    }

    private Assessment findAssessment(UUID assessmentId){
        Optional<Assessment> optionalAssessment = this.assessmentRepository.findById(assessmentId);
        if (optionalAssessment.isEmpty()){
            throw new NotFoundException("There are no assessment with this id.");
        }
        return optionalAssessment.get();
    }

    public void delete(UUID id){
        this.findAssessment(id);
        this.assessmentRepository.deleteById(id);
    }

    public void rename(UUID id, String name){
        Assessment assessment = this.findAssessment(id);
        assessment.setName(name);
        assessment.validate(messageSource);

        if (assessment.getGradingPeriod() != null) {
            var recoveryAssessmentOptional = this.recoveryAssessmentRepository.findByOriginalAssessmentId(assessment.getId());
            if (recoveryAssessmentOptional.isPresent()) {
                RecoveryAssessment recoveryAssessment = recoveryAssessmentOptional.get();
                recoveryAssessment.setName(assessment.getName() + " " + this.messageSource.getMessage("recovery", null, LocaleContextHolder.getLocale()));
                this.recoveryAssessmentRepository.save(recoveryAssessment);
            }
        }

        this.assessmentRepository.save(assessment);

    }
}
