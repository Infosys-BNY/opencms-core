package org.opencms.content.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentDefinitionDTO {
    
    private String entityId;
    private Map<String, ContentEntityDTO> entities;
    private Map<String, AttributeConfigurationDTO> configurations;
    private Map<String, TypeDTO> types;
    private List<TabInfoDTO> tabInfos;
    private String locale;
    private List<String> contentLocales;
    private Map<String, String> availableLocales;
    private List<String> synchronizations;
    private Map<String, String> syncValues;
    private Collection<String> skipPaths;
    private String title;
    private String sitePath;
    private String resourceType;
    private String iconClasses;
    private boolean performedAutocorrection;
    private boolean autoUnlock;
    private Set<String> editorChangeScopes;
    private boolean deleteOnCancel;
    private boolean isDirectEdit;
    private String newLink;
    
    public ContentDefinitionDTO() {
        this.entities = new HashMap<>();
        this.configurations = new HashMap<>();
        this.types = new HashMap<>();
        this.tabInfos = new ArrayList<>();
        this.contentLocales = new ArrayList<>();
        this.availableLocales = new LinkedHashMap<>();
        this.synchronizations = new ArrayList<>();
        this.syncValues = new HashMap<>();
        this.skipPaths = new HashSet<>();
        this.editorChangeScopes = new HashSet<>();
    }
    
    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    
    public Map<String, ContentEntityDTO> getEntities() { return entities; }
    public void setEntities(Map<String, ContentEntityDTO> entities) { this.entities = entities; }
    
    public Map<String, AttributeConfigurationDTO> getConfigurations() { return configurations; }
    public void setConfigurations(Map<String, AttributeConfigurationDTO> configurations) { 
        this.configurations = configurations; 
    }
    
    public Map<String, TypeDTO> getTypes() { return types; }
    public void setTypes(Map<String, TypeDTO> types) { this.types = types; }
    
    public List<TabInfoDTO> getTabInfos() { return tabInfos; }
    public void setTabInfos(List<TabInfoDTO> tabInfos) { this.tabInfos = tabInfos; }
    
    public String getLocale() { return locale; }
    public void setLocale(String locale) { this.locale = locale; }
    
    public List<String> getContentLocales() { return contentLocales; }
    public void setContentLocales(List<String> contentLocales) { this.contentLocales = contentLocales; }
    
    public Map<String, String> getAvailableLocales() { return availableLocales; }
    public void setAvailableLocales(Map<String, String> availableLocales) { 
        this.availableLocales = availableLocales; 
    }
    
    public List<String> getSynchronizations() { return synchronizations; }
    public void setSynchronizations(List<String> synchronizations) { 
        this.synchronizations = synchronizations; 
    }
    
    public Map<String, String> getSyncValues() { return syncValues; }
    public void setSyncValues(Map<String, String> syncValues) { this.syncValues = syncValues; }
    
    public Collection<String> getSkipPaths() { return skipPaths; }
    public void setSkipPaths(Collection<String> skipPaths) { this.skipPaths = skipPaths; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getSitePath() { return sitePath; }
    public void setSitePath(String sitePath) { this.sitePath = sitePath; }
    
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    
    public String getIconClasses() { return iconClasses; }
    public void setIconClasses(String iconClasses) { this.iconClasses = iconClasses; }
    
    public boolean isPerformedAutocorrection() { return performedAutocorrection; }
    public void setPerformedAutocorrection(boolean performedAutocorrection) { 
        this.performedAutocorrection = performedAutocorrection; 
    }
    
    public boolean isAutoUnlock() { return autoUnlock; }
    public void setAutoUnlock(boolean autoUnlock) { this.autoUnlock = autoUnlock; }
    
    public Set<String> getEditorChangeScopes() { return editorChangeScopes; }
    public void setEditorChangeScopes(Set<String> editorChangeScopes) { 
        this.editorChangeScopes = editorChangeScopes; 
    }
    
    public boolean isDeleteOnCancel() { return deleteOnCancel; }
    public void setDeleteOnCancel(boolean deleteOnCancel) { this.deleteOnCancel = deleteOnCancel; }
    
    public boolean isDirectEdit() { return isDirectEdit; }
    public void setDirectEdit(boolean directEdit) { isDirectEdit = directEdit; }
    
    public String getNewLink() { return newLink; }
    public void setNewLink(String newLink) { this.newLink = newLink; }
}
