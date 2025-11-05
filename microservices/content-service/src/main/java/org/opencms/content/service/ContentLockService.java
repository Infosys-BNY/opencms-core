package org.opencms.content.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ContentLockService {
    
    private static final Logger LOG = LoggerFactory.getLogger(ContentLockService.class);
    
    private final Map<UUID, String> locks = new ConcurrentHashMap<>();
    
    public void acquireLock(UUID structureId, String userId) {
        LOG.debug("Acquiring lock for resource: {} by user: {}", structureId, userId);
        locks.put(structureId, userId);
    }
    
    public void releaseLock(UUID structureId) {
        LOG.debug("Releasing lock for resource: {}", structureId);
        locks.remove(structureId);
    }
    
    public boolean isLocked(UUID structureId) {
        return locks.containsKey(structureId);
    }
    
    public String getLockOwner(UUID structureId) {
        return locks.get(structureId);
    }
}
