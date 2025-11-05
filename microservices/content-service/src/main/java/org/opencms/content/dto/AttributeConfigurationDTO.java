package org.opencms.content.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttributeConfigurationDTO {
    
    private String name;
    private String displayName;
    private String help;
    private String widgetName;
    private String widgetConfig;
    private String defaultValue;
    private boolean visible;
    private int minOccurs;
    private int maxOccurs;
    
    public AttributeConfigurationDTO() {}
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    
    public String getHelp() { return help; }
    public void setHelp(String help) { this.help = help; }
    
    public String getWidgetName() { return widgetName; }
    public void setWidgetName(String widgetName) { this.widgetName = widgetName; }
    
    public String getWidgetConfig() { return widgetConfig; }
    public void setWidgetConfig(String widgetConfig) { this.widgetConfig = widgetConfig; }
    
    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }
    
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    
    public int getMinOccurs() { return minOccurs; }
    public void setMinOccurs(int minOccurs) { this.minOccurs = minOccurs; }
    
    public int getMaxOccurs() { return maxOccurs; }
    public void setMaxOccurs(int maxOccurs) { this.maxOccurs = maxOccurs; }
}
