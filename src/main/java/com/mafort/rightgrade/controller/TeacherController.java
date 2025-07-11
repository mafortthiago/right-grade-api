package com.mafort.rightgrade.controller;

import com.mafort.rightgrade.domain.teacher.TeacherResponse;
import com.mafort.rightgrade.domain.teacher.TeacherService;
import com.mafort.rightgrade.domain.teacher.TeacherUpdateRequest;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("teachers")
public class TeacherController {

    @Autowired
    private TeacherService service;

    @GetMapping("/{id}")
    ResponseEntity<TeacherResponse> get(@PathVariable UUID id){
        TeacherResponse teacherResponse = this.service.get(id);
        return ResponseEntity.ok(teacherResponse);
    }

    @PatchMapping("/{id}")
    ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody @Valid TeacherUpdateRequest updateRequest){
        this.service.update(id, updateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id){
        this.service.deleteTeacherById(id);

        return ResponseEntity.noContent()
                .header("Set-Cookie", "token=; HttpOnly; Path=/; Max-Age=0")
                .header("Set-Cookie", "refreshToken=; HttpOnly; Path=/; Max-Age=0")
                .build();
    }
}
