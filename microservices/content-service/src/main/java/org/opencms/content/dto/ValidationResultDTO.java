package org.opencms.content.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationResultDTO {
    
    private Map<String, Map<String, ValidationMessageDTO>> errors;
    private Map<String, Map<String, ValidationMessageDTO>> warnings;
    
    public ValidationResultDTO() {
        this.errors = new HashMap<>();
        this.warnings = new HashMap<>();
    }
    
    public ValidationResultDTO(Map<String, Map<String, ValidationMessageDTO>> errors,
                              Map<String, Map<String, ValidationMessageDTO>> warnings) {
        this.errors = errors;
        this.warnings = warnings;
    }
    
    public Map<String, Map<String, ValidationMessageDTO>> getErrors() { return errors; }
    public void setErrors(Map<String, Map<String, ValidationMessageDTO>> errors) { 
        this.errors = errors; 
    }
    
    public Map<String, Map<String, ValidationMessageDTO>> getWarnings() { return warnings; }
    public void setWarnings(Map<String, Map<String, ValidationMessageDTO>> warnings) { 
        this.warnings = warnings; 
    }
    
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }
    
    public boolean hasWarnings() {
        return warnings != null && !warnings.isEmpty();
    }
    
    public boolean hasErrors(String entityId) {
        return errors != null && errors.containsKey(entityId);
    }
    
    public boolean hasWarnings(String entityId) {
        return warnings != null && warnings.containsKey(entityId);
    }
}
