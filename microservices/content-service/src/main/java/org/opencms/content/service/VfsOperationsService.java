package org.opencms.content.service;

import java.util.UUID;

public interface VfsOperationsService {
    
    byte[] readFile(UUID structureId);
    
    void writeFile(UUID structureId, byte[] content);
    
    void lockResource(UUID structureId);
    
    void unlockResource(UUID structureId);
    
    boolean resourceExists(UUID structureId);
}
