package com.mafort.rightgrade.domain.group;

import com.mafort.rightgrade.domain.grade.Grade;
import com.mafort.rightgrade.domain.gradingPeriod.GradingPeriod;
import com.mafort.rightgrade.domain.gradingPeriod.GradingPeriodResponse;
import com.mafort.rightgrade.domain.page.CustomPage;
import com.mafort.rightgrade.domain.student.StudentRepository;
import com.mafort.rightgrade.domain.teacher.Teacher;
import com.mafort.rightgrade.domain.teacher.TeacherRepository;
import com.mafort.rightgrade.infra.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    @Autowired
    private  MessageSource messageSource;

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

    public GroupResponseDTO findGroupResponseById(UUID id) {
        return new GroupResponseDTO(this.findGroupById(id));
    }

    private Group findGroupById(UUID id){
        Optional<Group> groupOptional = this.groupRepository.findById(id);
        if(groupOptional.isEmpty()){
            throw new NotFoundException("Group with this ID does not exist");
        }
        return groupOptional.get();
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

    public void rename(String name, UUID id){
        Group group = this.findGroupById(id);
        group.setName(name);
        group.validate(messageSource);
        this.groupRepository.save(group);
    }

    public void delete(UUID id){
        Group group = this.findGroupById(id);
        this.groupRepository.deleteById(id);
    }

    public void updateMinimumGrade(UUID id, Double value){
        Group group = this.findGroupById(id);
        group.setMinimumGrade(value);
        this.groupRepository.save(group);
    }
}