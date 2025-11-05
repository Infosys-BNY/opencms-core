package org.opencms.rendering.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "template_metadata")
public class TemplateMetadata {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, unique = true)
    private String templatePath;
    
    @Column(nullable = false)
    private String fileSystemPath;
    
    private long lastModified;
    private long lastCompiled;
    
    @Column(length = 1000)
    private String cacheKey;
    
    private boolean online;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public TemplateMetadata() {}
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTemplatePath() {
        return templatePath;
    }
    
    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }
    
    public String getFileSystemPath() {
        return fileSystemPath;
    }
    
    public void setFileSystemPath(String fileSystemPath) {
        this.fileSystemPath = fileSystemPath;
    }
    
    public long getLastModified() {
        return lastModified;
    }
    
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
    
    public long getLastCompiled() {
        return lastCompiled;
    }
    
    public void setLastCompiled(long lastCompiled) {
        this.lastCompiled = lastCompiled;
    }
    
    public String getCacheKey() {
        return cacheKey;
    }
    
    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
    
    public boolean isOnline() {
        return online;
    }
    
    public void setOnline(boolean online) {
        this.online = online;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
