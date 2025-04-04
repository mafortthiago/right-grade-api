package com.mafort.rightgrade.domain.group;

import com.mafort.rightgrade.domain.assessment.Assessment;
import com.mafort.rightgrade.domain.grade.Grade;
import com.mafort.rightgrade.domain.gradingPeriod.GradingPeriod;
import com.mafort.rightgrade.domain.gradingPeriod.GradingPeriodResponse;
import com.mafort.rightgrade.domain.page.CustomPage;
import com.mafort.rightgrade.domain.student.Student;
import com.mafort.rightgrade.domain.student.StudentRepository;
import com.mafort.rightgrade.domain.teacher.Teacher;
import com.mafort.rightgrade.domain.teacher.TeacherRepository;
import com.mafort.rightgrade.infra.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    public GroupService(TeacherRepository teacherRepository,
                        GroupRepository groupRepository,
                        StudentRepository studentRepository) {
        this.teacherRepository = teacherRepository;
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    public Group createGroup(CreateGroupDTO createGroupDTO) {
        Teacher teacher = teacherRepository.findById(createGroupDTO.teacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        return new Group(createGroupDTO, teacher);
    }

    public CustomPage<GroupListResponseDTO> findAllById(UUID id, Pageable pageable, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Group> pages = groupRepository.findByTeacherId(id, sortedPageable);
        CustomPage<Group> customPages = new CustomPage<>(pages);
        return customPages.map(p -> {
            int quantityStudents = this.studentRepository.getStudentsByGroupId(p.getId());
            return new GroupListResponseDTO(
                    p.getId(),
                    p.getName(),
                    p.getMinimumGrade(),
                    p.isGradeFrom0To100(),
                    p.getGradingPeriods().stream().map(GradingPeriodResponse::new).toList(),
                    quantityStudents,
                    getGradeAverage(p.getGradingPeriods())
            );
        });
    }

    public GroupResponseDTO findById(UUID id) {
        if(this.groupRepository.findById(id).isEmpty()){
            throw new NotFoundException("Group with this ID does not exist");
        }
        return new GroupResponseDTO(this.groupRepository.findById(id).get());
    }

    private double getGradeAverage(List<GradingPeriod> gradingPeriods){
        List<Grade> grades = gradingPeriods.stream()
                .flatMap(gradingPeriod -> gradingPeriod.getAssessments().stream())
                .flatMap(assessment -> assessment.getGrades().stream())
                .collect(Collectors.toList());

        return grades.stream()
                .mapToDouble(Grade::getValue)
                .average()
                .orElse(0.0);
    }
}