package com.mafort.rightgrade.domain.student;

import com.mafort.rightgrade.domain.grade.GradeResponse;
import com.mafort.rightgrade.domain.group.Group;
import com.mafort.rightgrade.domain.group.GroupRepository;
import com.mafort.rightgrade.infra.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentService {
    @Autowired
    private StudentRepository repository;
    @Autowired
    private GroupRepository groupRepository;

    public Student create(CreateStudentDTO studentDTO){
        Group group = this.findGroup(studentDTO.groupId());
        return this.repository.save(new Student(studentDTO, group));
    }

    public StudentResponse update(CreateStudentDTO studentDTO, UUID id){
        Group group = this.findGroup(studentDTO.groupId());
        Student student = new Student(id, studentDTO.name(), group);
        this.repository.save(student);
        return new StudentResponse(student);
    }

    public Group findGroup(UUID id){
        Optional<Group> groupOptional = this.groupRepository.findById(id);
        if(groupOptional.isEmpty()){
            throw new NotFoundException("There are no group with this id");
        }
        return groupOptional.get();
    }

    public List<Student> getAll(){
        return this.repository.findAll();
    }

    /**
     * Retrieves a list of students belonging to a specific group, along with their grades.
     *
     * @param groupId the UUID of the group for which the students should be retrieved.
     * @return a list of {@link StudentListResponse} objects containing the details of the students and their grades.
     *
     * This method queries the repository to get the students associated with the specified `groupId`.
     * Each student is represented by a {@link StudentListResponse} object, which includes the student's ID,
     * name, group ID, and a list of grades. If a student has no grades, the grades list will be empty.
     *
     * The method uses a map to ensure that each student is added only once to the response list,
     * even if the student has multiple grades. The grades are added to the corresponding student's grades list.
     *
     */
    public List<StudentListResponse> getStudentsByGroup(UUID groupId) {
        List<Object[]> results = this.repository.findStudentsByGroupId(groupId);
        Map<UUID, StudentListResponse> studentMap = new HashMap<>();

        for (Object[] result : results) {
            UUID id = (UUID) result[0];
            String name = (String) result[1];
            UUID groupIdResult = (UUID) result[2];
            Double gradeValue = (Double) result[3];
            UUID gradeId = (UUID) result[4];
            UUID assessmentId = (UUID) result[5];

            StudentListResponse student = studentMap.get(id);
            if (student == null) {
                student = new StudentListResponse(id, name, groupIdResult, new ArrayList<>());
                studentMap.put(id, student);
            }
            if (gradeValue != null){
                GradeResponse grade = new GradeResponse(gradeId,assessmentId,id,gradeValue);
                student.grades().add(grade);
            }

        }
        return new ArrayList<>(studentMap.values());
    }
}
