package org.opencms.user.service;

import org.opencms.user.dto.GroupDto;
import org.opencms.user.entity.Group;
import org.opencms.user.entity.User;
import org.opencms.user.exception.ResourceNotFoundException;
import org.opencms.user.repository.GroupRepository;
import org.opencms.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupService {
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public Group getGroupById(String groupId) {
        return groupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));
    }
    
    public Group getGroupByName(String groupName) {
        return groupRepository.findByGroupName(groupName)
            .orElseThrow(() -> new ResourceNotFoundException("Group not found with name: " + groupName));
    }
    
    @Transactional
    public Group createGroup(GroupDto groupDto) {
        if (groupRepository.existsByGroupName(groupDto.getGroupName())) {
            throw new IllegalArgumentException("Group name already exists");
        }
        
        Group group = new Group();
        group.setGroupName(groupDto.getGroupName());
        group.setGroupDescription(groupDto.getGroupDescription());
        group.setParentGroupId(groupDto.getParentGroupId());
        group.setGroupFlags(groupDto.getGroupFlags() != null ? groupDto.getGroupFlags() : 0);
        group.setGroupOu(groupDto.getGroupOu() != null ? groupDto.getGroupOu() : "/");
        
        return groupRepository.save(group);
    }
    
    @Transactional
    public Group updateGroup(String groupId, GroupDto groupDto) {
        Group group = getGroupById(groupId);
        
        if (groupDto.getGroupDescription() != null) {
            group.setGroupDescription(groupDto.getGroupDescription());
        }
        if (groupDto.getGroupFlags() != null) {
            group.setGroupFlags(groupDto.getGroupFlags());
        }
        if (groupDto.getParentGroupId() != null) {
            group.setParentGroupId(groupDto.getParentGroupId());
        }
        
        return groupRepository.save(group);
    }
    
    @Transactional
    public void deleteGroup(String groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new ResourceNotFoundException("Group not found with id: " + groupId);
        }
        groupRepository.deleteById(groupId);
    }
    
    @Transactional
    public void addUserToGroup(String groupId, String userId) {
        Group group = getGroupById(groupId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        group.getUsers().add(user);
        user.getGroups().add(group);
        
        groupRepository.save(group);
        userRepository.save(user);
    }
    
    @Transactional
    public void removeUserFromGroup(String groupId, String userId) {
        Group group = getGroupById(groupId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        group.getUsers().remove(user);
        user.getGroups().remove(group);
        
        groupRepository.save(group);
        userRepository.save(user);
    }
    
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }
    
    public GroupDto toDto(Group group) {
        GroupDto dto = new GroupDto();
        dto.setGroupId(group.getGroupId());
        dto.setGroupName(group.getGroupName());
        dto.setGroupDescription(group.getGroupDescription());
        dto.setParentGroupId(group.getParentGroupId());
        dto.setGroupFlags(group.getGroupFlags());
        dto.setGroupOu(group.getGroupOu());
        return dto;
    }
}
