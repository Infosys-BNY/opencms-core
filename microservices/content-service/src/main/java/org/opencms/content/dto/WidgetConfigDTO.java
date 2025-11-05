package org.opencms.content.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WidgetConfigDTO {
    
    private String widgetName;
    private String configuration;
    private Map<String, String> configurationParameters;
    
    public WidgetConfigDTO() {
        this.configurationParameters = new HashMap<>();
    }
    
    public WidgetConfigDTO(String widgetName, String configuration) {
        this();
        this.widgetName = widgetName;
        this.configuration = configuration;
    }
    
    public String getWidgetName() { return widgetName; }
    public void setWidgetName(String widgetName) { this.widgetName = widgetName; }
    
    public String getConfiguration() { return configuration; }
    public void setConfiguration(String configuration) { this.configuration = configuration; }
    
    public Map<String, String> getConfigurationParameters() { return configurationParameters; }
    public void setConfigurationParameters(Map<String, String> configurationParameters) { 
        this.configurationParameters = configurationParameters; 
    }
}
