package com.mafort.rightgrade.controller;

import com.mafort.rightgrade.domain.group.*;
import com.mafort.rightgrade.domain.page.CustomPage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RequestMapping("groups")
@RestController
public class GroupController{
    @Autowired
    private GroupRepository repository;
    @Autowired
    private GroupService service;

    @PostMapping
    public ResponseEntity<GroupResponseDTO> add(
            @RequestBody @Valid CreateGroupDTO groupDTO,
            UriComponentsBuilder uriComponentsBuilder)
    {
        var group = service.createGroup(groupDTO);
        repository.save(group);
        var uri = uriComponentsBuilder.path("/groups/{id}").buildAndExpand(group.getId()).toUri();
        return ResponseEntity.created(uri).body(new GroupResponseDTO(group));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomPage<GroupListResponseDTO>> findAllById
            (@PathVariable UUID id,
             Pageable pageable,
             @RequestParam(defaultValue = "createdAt") String sortBy,
             @RequestParam(defaultValue = "desc") String direction){
        return ResponseEntity.ok(this.service.findAllById(id, pageable, sortBy, direction));
    }
}