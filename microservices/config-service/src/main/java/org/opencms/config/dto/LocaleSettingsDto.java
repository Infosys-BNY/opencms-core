package org.opencms.config.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class LocaleSettingsDto {
    private Long id;
    
    @NotNull
    private String localeHandlerClass;
    
    @NotNull
    private List<String> configuredLocales;
    
    @NotNull
    private List<String> defaultLocales;
}
