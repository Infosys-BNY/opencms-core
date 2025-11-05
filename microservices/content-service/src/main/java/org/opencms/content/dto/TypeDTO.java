package org.opencms.content.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TypeDTO {
    
    private String id;
    private List<String> attributeNames;
    private Map<String, String> attributeTypeName;
    private Map<String, Integer> attributeMinOccurrence;
    private Map<String, Integer> attributeMaxOccurrence;
    
    public TypeDTO() {
        this.attributeNames = new ArrayList<>();
        this.attributeTypeName = new HashMap<>();
        this.attributeMinOccurrence = new HashMap<>();
        this.attributeMaxOccurrence = new HashMap<>();
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public List<String> getAttributeNames() { return attributeNames; }
    public void setAttributeNames(List<String> attributeNames) { 
        this.attributeNames = attributeNames; 
    }
    
    public Map<String, String> getAttributeTypeName() { return attributeTypeName; }
    public void setAttributeTypeName(Map<String, String> attributeTypeName) { 
        this.attributeTypeName = attributeTypeName; 
    }
    
    public Map<String, Integer> getAttributeMinOccurrence() { return attributeMinOccurrence; }
    public void setAttributeMinOccurrence(Map<String, Integer> attributeMinOccurrence) { 
        this.attributeMinOccurrence = attributeMinOccurrence; 
    }
    
    public Map<String, Integer> getAttributeMaxOccurrence() { return attributeMaxOccurrence; }
    public void setAttributeMaxOccurrence(Map<String, Integer> attributeMaxOccurrence) { 
        this.attributeMaxOccurrence = attributeMaxOccurrence; 
    }
}
