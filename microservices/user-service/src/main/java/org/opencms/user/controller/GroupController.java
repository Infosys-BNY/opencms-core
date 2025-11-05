package org.opencms.user.controller;

import org.opencms.user.dto.GroupDto;
import org.opencms.user.entity.Group;
import org.opencms.user.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    
    @Autowired
    private GroupService groupService;
    
    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable String id) {
        Group group = groupService.getGroupById(id);
        return ResponseEntity.ok(groupService.toDto(group));
    }
    
    @PostMapping
    public ResponseEntity<GroupDto> createGroup(@RequestBody GroupDto groupDto) {
        Group group = groupService.createGroup(groupDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.toDto(group));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<GroupDto> updateGroup(@PathVariable String id, @RequestBody GroupDto groupDto) {
        Group group = groupService.updateGroup(id, groupDto);
        return ResponseEntity.ok(groupService.toDto(group));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable String id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/users/{userId}")
    public ResponseEntity<Void> addUserToGroup(@PathVariable String id, @PathVariable String userId) {
        groupService.addUserToGroup(id, userId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}/users/{userId}")
    public ResponseEntity<Void> removeUserFromGroup(@PathVariable String id, @PathVariable String userId) {
        groupService.removeUserFromGroup(id, userId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping
    public ResponseEntity<List<GroupDto>> getAllGroups() {
        List<Group> groups = groupService.getAllGroups();
        List<GroupDto> groupDtos = groups.stream()
            .map(group -> groupService.toDto(group))
            .collect(Collectors.toList());
        return ResponseEntity.ok(groupDtos);
    }
}
