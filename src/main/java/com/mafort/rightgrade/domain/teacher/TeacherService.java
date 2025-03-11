package com.mafort.rightgrade.domain.teacher;

import com.mafort.rightgrade.infra.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository repository;

    public TeacherResponse get(UUID id){
        Teacher teacher = this.findTeacherById(id);
        return new TeacherResponse(teacher.getName(), teacher.getEmail());
    }

    private Teacher findTeacherById(UUID id){
        Optional<Teacher> teacherOptional = this.repository.findById(id);
        if(teacherOptional.isEmpty()){
            throw  new NotFoundException("There is no teacher with this id");
        }
        return teacherOptional.get();
    }

    public void update(UUID id, TeacherUpdateRequest teacherUpdateRequest){
        Teacher teacher = this.findTeacherById(id);
        teacher.setName(teacherUpdateRequest.name());
        this.repository.save(teacher);
    }

}
