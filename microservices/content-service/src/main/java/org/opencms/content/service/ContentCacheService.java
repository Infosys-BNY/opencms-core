package org.opencms.content.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ContentCacheService {
    
    private static final Logger LOG = LoggerFactory.getLogger(ContentCacheService.class);
    
    private final Cache<String, Object> contentCache;
    private final Cache<String, Object> dynamicValueCache;
    
    public ContentCacheService() {
        this.contentCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();
            
        this.dynamicValueCache = Caffeine.newBuilder()
            .maximumSize(500)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();
    }
    
    public void cacheXmlContent(UUID structureId, Object content) {
        contentCache.put(structureId.toString(), content);
    }
    
    public Object getCachedXmlContent(UUID structureId) {
        return contentCache.getIfPresent(structureId.toString());
    }
    
    public void uncacheXmlContent(UUID structureId) {
        contentCache.invalidate(structureId.toString());
    }
    
    public void clearDynamicValues() {
        dynamicValueCache.invalidateAll();
    }
    
    public void setDynamicValue(String key, String value) {
        dynamicValueCache.put(key, value);
    }
    
    public String getDynamicValue(String key) {
        Object value = dynamicValueCache.getIfPresent(key);
        return value != null ? value.toString() : null;
    }
}
