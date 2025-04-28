package com.mafort.rightgrade.domain.grade;

import com.mafort.rightgrade.domain.assessment.*;
import com.mafort.rightgrade.domain.student.Student;
import com.mafort.rightgrade.domain.student.StudentRepository;
import com.mafort.rightgrade.infra.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GradeService {
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    RecoveryAssessmentRepository recoveryAssessmentRepository;

    public UUID add(CreateGrade createGrade){
        Optional<Assessment> assessmentOptional = this.assessmentRepository.findById(createGrade.assessmentId());
        Optional<RecoveryAssessment> recoveryAssessmentOptional = this.recoveryAssessmentRepository.findById(createGrade.assessmentId());
        if(assessmentOptional.isEmpty() && recoveryAssessmentOptional.isEmpty()){
            throw new NotFoundException("There isn't any assessment with this id.");
        }

        Optional<Student> studentOptional = this.studentRepository.findById(createGrade.studentId());
        if(studentOptional.isEmpty()){
            throw  new NotFoundException("There isn't any student with this id.");
        }
        Student student = studentOptional.get();

        Grade grade;
        if(assessmentOptional.isPresent()){
             grade = new Grade(createGrade, assessmentOptional.get(), student);
        } else {
             grade = new Grade(createGrade, recoveryAssessmentOptional.get(), student);
        }

        this.gradeRepository.save(grade);

        return grade.getId();
    }

    public void update(CreateGrade createGrade, UUID id){
        Optional<Grade> gradeOptional = this.gradeRepository.findById(id);
        if(gradeOptional.isEmpty()){
            throw new NotFoundException("No grade found with the given id");
        }
        Grade grade = gradeOptional.get();
        grade.setValue(createGrade.value());
        this.gradeRepository.save(grade);
    }
}
