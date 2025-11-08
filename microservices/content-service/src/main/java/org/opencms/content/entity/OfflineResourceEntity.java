package org.opencms.content.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "CMS_OFFLINE_RESOURCES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfflineResourceEntity {
    
    @Id
    @Column(name = "RESOURCE_ID", length = 36)
    private String resourceId;
    
    @Column(name = "RESOURCE_TYPE", nullable = false)
    private Integer resourceType;
    
    @Column(name = "RESOURCE_FLAGS", nullable = false)
    private Integer resourceFlags;
    
    @Column(name = "RESOURCE_STATE", nullable = false)
    private Integer resourceState;
    
    @Column(name = "RESOURCE_SIZE", nullable = false)
    private Integer resourceSize;
    
    @Column(name = "DATE_CONTENT", nullable = false)
    private Long dateContent;
    
    @Column(name = "SIBLING_COUNT", nullable = false)
    private Integer siblingCount;
    
    @Column(name = "DATE_CREATED", nullable = false)
    private Long dateCreated;
    
    @Column(name = "DATE_LASTMODIFIED", nullable = false)
    private Long dateLastModified;
    
    @Column(name = "USER_CREATED", nullable = false, length = 36)
    private String userCreated;
    
    @Column(name = "USER_LASTMODIFIED", nullable = false, length = 36)
    private String userLastModified;
    
    @Column(name = "PROJECT_LASTMODIFIED", nullable = false, length = 36)
    private String projectLastModified;
    
    @Column(name = "RESOURCE_VERSION", nullable = false)
    private Integer resourceVersion;
}
