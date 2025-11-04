package org.opencms.user.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "CMS_GROUPS")
public class Group {
    
    @Id
    @Column(name = "GROUP_ID", length = 36)
    private String groupId;
    
    @Column(name = "GROUP_NAME", nullable = false)
    private String groupName;
    
    @Column(name = "GROUP_DESCRIPTION")
    private String groupDescription;
    
    @Column(name = "PARENT_GROUP_ID", length = 36)
    private String parentGroupId;
    
    @Column(name = "GROUP_FLAGS")
    private Integer groupFlags;
    
    @Column(name = "GROUP_OU")
    private String groupOu;
    
    @ManyToMany(mappedBy = "groups")
    private Set<User> users = new HashSet<>();
    
    public Group() {
        this.groupId = UUID.randomUUID().toString();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(String parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    public Integer getGroupFlags() {
        return groupFlags;
    }

    public void setGroupFlags(Integer groupFlags) {
        this.groupFlags = groupFlags;
    }

    public String getGroupOu() {
        return groupOu;
    }

    public void setGroupOu(String groupOu) {
        this.groupOu = groupOu;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
