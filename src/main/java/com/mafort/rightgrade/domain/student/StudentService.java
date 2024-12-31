package com.mafort.rightgrade.domain.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository repository;
    public Student create(CreateStudentDTO studentDTO){
        return this.repository.save(new Student(studentDTO));
    }

    public List<Student> getAll(){
        return this.repository.findAll();
    }
}
