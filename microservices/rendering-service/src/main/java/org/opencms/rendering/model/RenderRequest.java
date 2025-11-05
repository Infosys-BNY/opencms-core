package org.opencms.rendering.model;

import java.util.Map;

public class RenderRequest {
    private String templatePath;
    private String templateContent;
    private Map<String, Object> contextData;
    private CacheMode cacheMode;
    private boolean online;
    
    public RenderRequest() {
        this.cacheMode = CacheMode.NORMAL;
        this.online = true;
    }
    
    public String getTemplatePath() {
        return templatePath;
    }
    
    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }
    
    public String getTemplateContent() {
        return templateContent;
    }
    
    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }
    
    public Map<String, Object> getContextData() {
        return contextData;
    }
    
    public void setContextData(Map<String, Object> contextData) {
        this.contextData = contextData;
    }
    
    public CacheMode getCacheMode() {
        return cacheMode;
    }
    
    public void setCacheMode(CacheMode cacheMode) {
        this.cacheMode = cacheMode;
    }
    
    public boolean isOnline() {
        return online;
    }
    
    public void setOnline(boolean online) {
        this.online = online;
    }
}
