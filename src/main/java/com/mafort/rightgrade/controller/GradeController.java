package com.mafort.rightgrade.controller;

import com.mafort.rightgrade.domain.grade.CreateGrade;
import com.mafort.rightgrade.domain.grade.GradeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;
@RequestMapping("grades")
@RestController
public class GradeController {
    @Autowired
    private GradeService service;

    @PostMapping
    public ResponseEntity<Void> add(@Valid @RequestBody CreateGrade createGrade, UriComponentsBuilder uriBuilder){
        UUID idCreated = this.service.add(createGrade);
        URI uri = uriBuilder.path("grades/{id}").buildAndExpand(idCreated).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public  ResponseEntity<Void> update(@Valid @RequestBody CreateGrade createGrade, @PathVariable UUID id){
        this.service.update(createGrade, id);
        return ResponseEntity.ok().build();
    }
}
