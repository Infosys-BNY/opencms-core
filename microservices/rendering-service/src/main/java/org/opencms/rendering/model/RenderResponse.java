package org.opencms.rendering.model;

public class RenderResponse {
    private String content;
    private String contentType;
    private long lastModified;
    private boolean cached;
    
    public RenderResponse() {}
    
    public RenderResponse(String content, String contentType) {
        this.content = content;
        this.contentType = contentType;
        this.lastModified = System.currentTimeMillis();
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public long getLastModified() {
        return lastModified;
    }
    
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
    
    public boolean isCached() {
        return cached;
    }
    
    public void setCached(boolean cached) {
        this.cached = cached;
    }
}
