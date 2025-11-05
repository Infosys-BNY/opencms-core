package org.opencms.content.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TabInfoDTO {
    
    private String tabName;
    private String tabId;
    private String description;
    private String startName;
    private boolean collapsed;
    
    public TabInfoDTO() {}
    
    public TabInfoDTO(String tabName, String tabId, String description, String startName, boolean collapsed) {
        this.tabName = tabName;
        this.tabId = tabId;
        this.description = description;
        this.startName = startName;
        this.collapsed = collapsed;
    }
    
    public String getTabName() { return tabName; }
    public void setTabName(String tabName) { this.tabName = tabName; }
    
    public String getTabId() { return tabId; }
    public void setTabId(String tabId) { this.tabId = tabId; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getStartName() { return startName; }
    public void setStartName(String startName) { this.startName = startName; }
    
    public boolean isCollapsed() { return collapsed; }
    public void setCollapsed(boolean collapsed) { this.collapsed = collapsed; }
}
