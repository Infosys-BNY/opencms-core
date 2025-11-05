package org.opencms.publishing.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "CMS_PUBLISH_HISTORY")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishHistoryEntity {
    
    @Id
    @Column(name = "PUBLISH_TAG")
    private Integer publishTag;
    
    @Column(name = "STRUCTURE_ID", nullable = false)
    private String structureId;
    
    @Column(name = "RESOURCE_ID", nullable = false)
    private String resourceId;
    
    @Column(name = "RESOURCE_PATH", nullable = false, length = 1024)
    private String resourcePath;
    
    @Column(name = "RESOURCE_STATE", nullable = false)
    private Integer movedState;
    
    @Column(name = "RESOURCE_TYPE", nullable = false)
    private Integer resourceType;
    
    @Column(name = "HISTORY_ID", nullable = false)
    private String historyId;
    
    @Column(name = "SIBLING_COUNT", nullable = false)
    private Integer siblingCount;
}
