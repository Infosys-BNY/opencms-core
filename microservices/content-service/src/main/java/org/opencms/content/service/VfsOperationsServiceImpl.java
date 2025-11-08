package org.opencms.content.service;

import org.opencms.content.entity.*;
import org.opencms.content.repository.*;
import org.opencms.content.exception.ResourceNotFoundException;
import org.opencms.content.exception.ResourceLockedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class VfsOperationsServiceImpl implements VfsOperationsService {
    
    private static final Logger LOG = LoggerFactory.getLogger(VfsOperationsServiceImpl.class);
    
    @Autowired
    private OfflineStructureRepository offlineStructureRepository;
    
    @Autowired
    private OfflineResourceRepository offlineResourceRepository;
    
    @Autowired
    private OfflineContentRepository offlineContentRepository;
    
    @Autowired
    private ResourceLockRepository resourceLockRepository;
    
    @Override
    @Transactional(readOnly = true)
    public byte[] readFile(UUID structureId) {
        LOG.debug("Reading file for structure ID: {}", structureId);
        String id = structureId.toString();
        
        OfflineStructureEntity structure = offlineStructureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Structure not found: " + id));
        
        OfflineContentEntity content = offlineContentRepository.findById(structure.getResourceId())
            .orElseThrow(() -> new ResourceNotFoundException("Content not found for resource: " + structure.getResourceId()));
        
        LOG.debug("Successfully read {} bytes for structure {}", content.getFileContent().length, structureId);
        return content.getFileContent();
    }
    
    @Override
    @Transactional
    public void writeFile(UUID structureId, byte[] content) {
        LOG.info("Writing file for structure ID: {}, size: {} bytes", structureId, content.length);
        String id = structureId.toString();
        
        OfflineStructureEntity structure = offlineStructureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Structure not found: " + id));
        
        checkLock(structure.getResourcePath());
        
        OfflineContentEntity contentEntity = offlineContentRepository.findById(structure.getResourceId())
            .orElse(new OfflineContentEntity(structure.getResourceId(), new byte[0]));
        
        contentEntity.setFileContent(content);
        offlineContentRepository.save(contentEntity);
        
        OfflineResourceEntity resource = offlineResourceRepository.findById(structure.getResourceId())
            .orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + structure.getResourceId()));
        
        resource.setDateLastModified(System.currentTimeMillis());
        resource.setResourceSize(content.length);
        offlineResourceRepository.save(resource);
        
        LOG.info("Successfully wrote file for structure {}", structureId);
    }
    
    @Override
    @Transactional
    public void lockResource(UUID structureId) {
        LOG.info("Locking resource with structure ID: {}", structureId);
        String id = structureId.toString();
        
        OfflineStructureEntity structure = offlineStructureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Structure not found: " + id));
        
        Optional<ResourceLockEntity> existingLock = resourceLockRepository.findByResourcePath(structure.getResourcePath());
        if (existingLock.isPresent()) {
            LOG.warn("Resource already locked: {}", structure.getResourcePath());
            throw new ResourceLockedException("Resource is already locked: " + structure.getResourcePath());
        }
        
        ResourceLockEntity lock = new ResourceLockEntity();
        lock.setResourcePath(structure.getResourcePath());
        lock.setUserId("system");
        lock.setProjectId("offline");
        lock.setLockType(1);
        resourceLockRepository.save(lock);
        
        LOG.info("Successfully locked resource: {}", structure.getResourcePath());
    }
    
    @Override
    @Transactional
    public void unlockResource(UUID structureId) {
        LOG.info("Unlocking resource with structure ID: {}", structureId);
        String id = structureId.toString();
        
        OfflineStructureEntity structure = offlineStructureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Structure not found: " + id));
        
        resourceLockRepository.deleteByResourcePath(structure.getResourcePath());
        LOG.info("Successfully unlocked resource: {}", structure.getResourcePath());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean resourceExists(UUID structureId) {
        boolean exists = offlineStructureRepository.existsById(structureId.toString());
        LOG.debug("Resource exists check for {}: {}", structureId, exists);
        return exists;
    }
    
    @Transactional(readOnly = true)
    public OfflineStructureEntity getStructure(UUID structureId) {
        LOG.debug("Getting structure for ID: {}", structureId);
        return offlineStructureRepository.findById(structureId.toString())
            .orElseThrow(() -> new ResourceNotFoundException("Structure not found: " + structureId));
    }
    
    @Transactional(readOnly = true)
    public OfflineResourceEntity getResource(UUID structureId) {
        LOG.debug("Getting resource for structure ID: {}", structureId);
        OfflineStructureEntity structure = getStructure(structureId);
        return offlineResourceRepository.findById(structure.getResourceId())
            .orElseThrow(() -> new ResourceNotFoundException("Resource not found for structure: " + structureId));
    }
    
    private void checkLock(String resourcePath) {
        Optional<ResourceLockEntity> lock = resourceLockRepository.findByResourcePath(resourcePath);
        if (lock.isPresent()) {
            throw new ResourceLockedException("Resource is locked: " + resourcePath);
        }
    }
}
