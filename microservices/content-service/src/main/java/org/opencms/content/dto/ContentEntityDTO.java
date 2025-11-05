package org.opencms.content.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentEntityDTO {
    
    private String id;
    private String typeName;
    private Map<String, List<String>> simpleAttributes;
    private Map<String, List<ContentEntityDTO>> entityAttributes;
    
    public ContentEntityDTO() {
        this.simpleAttributes = new HashMap<>();
        this.entityAttributes = new HashMap<>();
    }
    
    public ContentEntityDTO(String id, String typeName) {
        this();
        this.id = id;
        this.typeName = typeName;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    
    public Map<String, List<String>> getSimpleAttributes() { return simpleAttributes; }
    public void setSimpleAttributes(Map<String, List<String>> simpleAttributes) { 
        this.simpleAttributes = simpleAttributes; 
    }
    
    public Map<String, List<ContentEntityDTO>> getEntityAttributes() { return entityAttributes; }
    public void setEntityAttributes(Map<String, List<ContentEntityDTO>> entityAttributes) { 
        this.entityAttributes = entityAttributes; 
    }
    
    public void addSimpleAttribute(String name, String value) {
        simpleAttributes.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
    }
    
    public void addEntityAttribute(String name, ContentEntityDTO entity) {
        entityAttributes.computeIfAbsent(name, k -> new ArrayList<>()).add(entity);
    }
}
