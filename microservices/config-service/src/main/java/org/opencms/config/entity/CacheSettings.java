package org.opencms.config.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cache_settings")
@Data
public class CacheSettings {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Boolean flexCacheEnabled;
    
    @Column(nullable = false)
    private Boolean flexCacheOffline;
    
    @Column
    private Long maxCacheBytes;
    
    @Column
    private Long avgCacheBytes;
    
    @Column
    private Long maxEntryBytes;
    
    @Column
    private Integer maxKeys;
    
    @Column
    private Integer sizeUsers;
    
    @Column
    private Integer sizeGroups;
    
    @Column
    private Integer sizeOrgUnits;
    
    @Column
    private Integer sizeResources;
    
    @Column
    private Integer sizeProperties;
}
