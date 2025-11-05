package org.opencms.config.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SystemSettingsDto {
    private Long id;
    
    @NotNull
    private String defaultContentEncoding;
    
    @NotNull
    private String timezone;
    
    @NotNull
    private Boolean historyEnabled;
    
    @NotNull
    private Integer historyVersions;
    
    @NotNull
    private Integer historyVersionsAfterDeletion;
    
    private Boolean saxImplSystemProperties;
    private String notificationProject;
    private Integer notificationTime;
    private String userSessionMode;
}
