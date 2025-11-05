package org.opencms.config.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.opencms.config.entity.HandlerConfig.HandlerType;
import java.util.Map;

@Data
public class HandlerConfigDto {
    private Long id;
    
    @NotNull
    private HandlerType type;
    
    @NotNull
    private String className;
    
    private Integer orderIndex;
    private Map<String, String> parameters;
}
