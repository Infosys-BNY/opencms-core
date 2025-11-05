package org.opencms.content.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class VfsOperationsServiceImpl implements VfsOperationsService {
    
    private static final Logger LOG = LoggerFactory.getLogger(VfsOperationsServiceImpl.class);
    
    @Override
    public byte[] readFile(UUID structureId) {
        LOG.warn("VFS stub: readFile called for {}", structureId);
        return new byte[0];
    }
    
    @Override
    public void writeFile(UUID structureId, byte[] content) {
        LOG.warn("VFS stub: writeFile called for {}", structureId);
    }
    
    @Override
    public void lockResource(UUID structureId) {
        LOG.warn("VFS stub: lockResource called for {}", structureId);
    }
    
    @Override
    public void unlockResource(UUID structureId) {
        LOG.warn("VFS stub: unlockResource called for {}", structureId);
    }
    
    @Override
    public boolean resourceExists(UUID structureId) {
        LOG.warn("VFS stub: resourceExists called for {}", structureId);
        return false;
    }
}
