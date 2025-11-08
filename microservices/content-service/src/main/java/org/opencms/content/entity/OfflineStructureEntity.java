package org.opencms.content.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "CMS_OFFLINE_STRUCTURE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfflineStructureEntity {
    
    @Id
    @Column(name = "STRUCTURE_ID", length = 36)
    private String structureId;
    
    @Column(name = "RESOURCE_ID", nullable = false, length = 36)
    private String resourceId;
    
    @Column(name = "PARENT_ID", nullable = false, length = 36)
    private String parentId;
    
    @Column(name = "RESOURCE_PATH", length = 1024)
    private String resourcePath;
    
    @Column(name = "STRUCTURE_STATE", nullable = false)
    private Integer structureState;
    
    @Column(name = "DATE_RELEASED", nullable = false)
    private Long dateReleased;
    
    @Column(name = "DATE_EXPIRED", nullable = false)
    private Long dateExpired;
    
    @Column(name = "STRUCTURE_VERSION", nullable = false)
    private Integer structureVersion;
}
