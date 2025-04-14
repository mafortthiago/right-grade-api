package com.mafort.rightgrade.controller;

import com.mafort.rightgrade.domain.group.*;
import com.mafort.rightgrade.domain.page.CustomPage;
import com.mafort.rightgrade.infra.exception.InvalidArgumentException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    @Autowired
    private MessageSource messageSource;

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

    @GetMapping("/byTeacher/{id}")
    public ResponseEntity<CustomPage<GroupListResponseDTO>> findAllById
            (@PathVariable UUID id,
             Pageable pageable,
             @RequestParam(defaultValue = "createdAt") String sortBy,
             @RequestParam(defaultValue = "desc") String direction){
        return ResponseEntity.ok(this.service.findAllById(id, pageable, sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponseDTO> findById(@PathVariable UUID id){
        return ResponseEntity.ok(this.service.findGroupResponseById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody UpdateGroupRequest request){
        if(request.name() != null && (!request.name().isEmpty())){
            this.service.rename(request.name(), id);
        }else{
            throw new InvalidArgumentException(
                    messageSource.getMessage(
                            "error.class.notExistsArgument",
                            null,
                            LocaleContextHolder.getLocale()));
        };
        return ResponseEntity.ok().build();
    }
}