package org.opencms.content.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "CMS_RESOURCE_LOCKS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceLockEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOCK_ID")
    private Long lockId;
    
    @Column(name = "RESOURCE_PATH", length = 1024)
    private String resourcePath;
    
    @Column(name = "USER_ID", nullable = false, length = 36)
    private String userId;
    
    @Column(name = "PROJECT_ID", nullable = false, length = 36)
    private String projectId;
    
    @Column(name = "LOCK_TYPE", nullable = false)
    private Integer lockType;
}
