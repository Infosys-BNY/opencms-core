package org.opencms.user.dto;

public class GroupDto {
    
    private String groupId;
    private String groupName;
    private String groupDescription;
    private String parentGroupId;
    private Integer groupFlags;
    private String groupOu;

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
}
