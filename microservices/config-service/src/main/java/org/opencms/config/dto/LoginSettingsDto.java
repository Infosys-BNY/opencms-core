package org.opencms.config.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginSettingsDto {
    private Long id;
    
    @NotNull
    private Integer disableMinutes;
    
    @NotNull
    private Integer maxBadAttempts;
}
