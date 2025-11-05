package org.opencms.config.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CacheSettingsDto {
    private Long id;
    
    @NotNull
    private Boolean flexCacheEnabled;
    
    @NotNull
    private Boolean flexCacheOffline;
    
    private Long maxCacheBytes;
    private Long avgCacheBytes;
    private Long maxEntryBytes;
    private Integer maxKeys;
    private Integer sizeUsers;
    private Integer sizeGroups;
    private Integer sizeOrgUnits;
    private Integer sizeResources;
    private Integer sizeProperties;
}
