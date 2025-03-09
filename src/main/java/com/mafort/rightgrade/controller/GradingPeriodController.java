package com.mafort.rightgrade.controller;

import com.mafort.rightgrade.domain.gradingPeriod.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequestMapping("gradingPeriods")
@RestController
public class GradingPeriodController {
    @Autowired
    private GradingPeriodService gradingPeriodService;

    @PostMapping
    public ResponseEntity<GradingPeriodResponse> create(@Valid @RequestBody  CreateGradingPeriod createGradingPeriod, UriComponentsBuilder uriComponentsBuilder){
        GradingPeriod gradingPeriod = this.gradingPeriodService.create(createGradingPeriod);
        URI uri = uriComponentsBuilder.path("gradingPeriods/{id}").buildAndExpand(gradingPeriod.getId()).toUri();
        return ResponseEntity.created(uri).body(new GradingPeriodResponse(gradingPeriod));
    }

    @PutMapping("/{gradingPeriodId}")
    public ResponseEntity<Void> edit(@Valid @RequestBody EditGradingPeriod editGradingPeriod, @PathVariable UUID gradingPeriodId){
        this.gradingPeriodService.edit(editGradingPeriod, gradingPeriodId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        this.gradingPeriodService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

