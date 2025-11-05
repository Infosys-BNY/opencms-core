package org.opencms.content.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationIssueDTO {
    
    private List<PathElementDTO> path;
    private String message;
    private Integer errorCount;
    
    public ValidationIssueDTO() {
        this.path = new ArrayList<>();
    }
    
    public ValidationIssueDTO(List<PathElementDTO> path, String message) {
        this.path = path;
        this.message = message;
    }
    
    public List<PathElementDTO> getPath() { return path; }
    public void setPath(List<PathElementDTO> path) { this.path = path; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Integer getErrorCount() { return errorCount; }
    public void setErrorCount(Integer errorCount) { this.errorCount = errorCount; }
    
    public static class PathElementDTO {
        private String name;
        private Integer index;
        
        public PathElementDTO() {}
        
        public PathElementDTO(String name, Integer index) {
            this.name = name;
            this.index = index;
        }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public Integer getIndex() { return index; }
        public void setIndex(Integer index) { this.index = index; }
    }
}
