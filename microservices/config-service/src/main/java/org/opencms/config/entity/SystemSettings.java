package org.opencms.config.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "system_settings")
@Data
public class SystemSettings {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String defaultContentEncoding;
    
    @Column(nullable = false)
    private String timezone;
    
    @Column(nullable = false)
    private Boolean historyEnabled;
    
    @Column(nullable = false)
    private Integer historyVersions;
    
    @Column(nullable = false)
    private Integer historyVersionsAfterDeletion;
    
    @Column(nullable = false)
    private Boolean saxImplSystemProperties;
    
    @Column
    private String notificationProject;
    
    @Column
    private Integer notificationTime;
    
    @Column
    private String userSessionMode;
}
