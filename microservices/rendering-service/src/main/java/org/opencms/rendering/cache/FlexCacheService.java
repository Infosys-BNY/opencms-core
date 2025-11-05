package org.opencms.rendering.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class FlexCacheService {
    
    @Cacheable(value = "fragmentCache", key = "#cacheKey + '-' + #variation")
    public byte[] getCachedFragment(String cacheKey, String variation) {
        return null;
    }
    
    public void putFragment(String cacheKey, String variation, byte[] content) {
    }
    
    @CacheEvict(value = "fragmentCache", allEntries = true)
    public void clearAll() {
    }
    
    @CacheEvict(value = "fragmentCache", key = "#cacheKey")
    public void clearResource(String cacheKey) {
    }
}
