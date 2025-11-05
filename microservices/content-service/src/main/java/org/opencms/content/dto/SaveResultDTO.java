package org.opencms.content.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SaveResultDTO {
    
    private boolean hasChangedSettings;
    private boolean warningsAsError;
    private ValidationResultDTO validationResult;
    private Map<String, List<ValidationIssueDTO>> validationIssueInformation;
    
    public SaveResultDTO() {
        this.validationIssueInformation = new HashMap<>();
    }
    
    public SaveResultDTO(boolean hasChangedSettings, ValidationResultDTO validationResult, 
                         boolean warningsAsError, Map<String, List<ValidationIssueDTO>> issues) {
        this.hasChangedSettings = hasChangedSettings;
        this.validationResult = validationResult;
        this.warningsAsError = warningsAsError;
        this.validationIssueInformation = issues;
    }
    
    public boolean isHasChangedSettings() { return hasChangedSettings; }
    public void setHasChangedSettings(boolean hasChangedSettings) { 
        this.hasChangedSettings = hasChangedSettings; 
    }
    
    public boolean isWarningsAsError() { return warningsAsError; }
    public void setWarningsAsError(boolean warningsAsError) { 
        this.warningsAsError = warningsAsError; 
    }
    
    public ValidationResultDTO getValidationResult() { return validationResult; }
    public void setValidationResult(ValidationResultDTO validationResult) { 
        this.validationResult = validationResult; 
    }
    
    public Map<String, List<ValidationIssueDTO>> getValidationIssueInformation() { 
        return validationIssueInformation; 
    }
    public void setValidationIssueInformation(Map<String, List<ValidationIssueDTO>> validationIssueInformation) { 
        this.validationIssueInformation = validationIssueInformation; 
    }
    
    public boolean hasErrors() {
        return validationResult != null && 
               (validationResult.hasErrors() || (warningsAsError && validationResult.hasWarnings()));
    }
}
