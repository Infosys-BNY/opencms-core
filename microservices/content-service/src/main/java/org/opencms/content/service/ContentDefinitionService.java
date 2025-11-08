package org.opencms.content.service;

import org.dom4j.Document;
import org.opencms.content.dto.ContentDefinitionDTO;
import org.opencms.content.dto.ContentEntityDTO;
import org.opencms.content.entity.OfflineStructureEntity;
import org.opencms.content.entity.OfflineResourceEntity;
import org.opencms.content.exception.ContentServiceException;
import org.opencms.content.util.XmlContentParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ContentDefinitionService {
    
    private static final Logger LOG = LoggerFactory.getLogger(ContentDefinitionService.class);
    
    private final VfsOperationsServiceImpl vfsService;
    
    @Autowired
    public ContentDefinitionService(VfsOperationsServiceImpl vfsService) {
        this.vfsService = vfsService;
    }
    
    @Transactional(readOnly = true)
    @Cacheable(value = "contentDefinitions", key = "#entityId")
    public ContentDefinitionDTO loadInitialDefinition(
            String entityId, 
            String clientId, 
            String newLink,
            UUID modelFileId,
            String editContext,
            String mainLocale,
            String mode,
            String postCreateHandler,
            Map<String, String> settingPresets,
            String editorStylesheet) {
        
        LOG.info("Loading initial definition for entity: {}", entityId);
        
        try {
            UUID structureId = parseEntityId(entityId);
            
            OfflineStructureEntity structure = vfsService.getStructure(structureId);
            OfflineResourceEntity resource = vfsService.getResource(structureId);
            byte[] contentBytes = vfsService.readFile(structureId);
            
            Document xmlDoc = XmlContentParser.parseXml(contentBytes);
            
            ContentDefinitionDTO definition = new ContentDefinitionDTO();
            definition.setEntityId(entityId);
            definition.setSitePath(structure.getResourcePath());
            definition.setResourceType(String.valueOf(resource.getResourceType()));
            definition.setLocale(mainLocale != null ? mainLocale : "en");
            
            ContentEntityDTO entity = XmlContentParser.documentToEntity(
                xmlDoc, entityId, definition.getLocale());
            definition.getEntities().put(entityId, entity);
            
            definition.getAvailableLocales().put("en", "English");
            definition.getContentLocales().add("en");
            
            LOG.info("Successfully loaded initial definition for entity: {}", entityId);
            return definition;
            
        } catch (Exception e) {
            LOG.error("Error loading initial definition for entity: {}", entityId, e);
            throw new ContentServiceException("Failed to load initial definition: " + e.getMessage(), e);
        }
    }
    
    @Transactional(readOnly = true)
    public ContentDefinitionDTO loadDefinition(
            String entityId,
            String clientId,
            ContentEntityDTO editedLocaleEntity,
            Collection<String> skipPaths,
            Map<String, String> settingPresets) {
        
        LOG.info("Loading definition for entity: {}", entityId);
        
        try {
            UUID structureId = parseEntityId(entityId);
            
            OfflineStructureEntity structure = vfsService.getStructure(structureId);
            OfflineResourceEntity resource = vfsService.getResource(structureId);
            byte[] contentBytes = vfsService.readFile(structureId);
            
            Document xmlDoc = XmlContentParser.parseXml(contentBytes);
            
            ContentDefinitionDTO definition = new ContentDefinitionDTO();
            definition.setEntityId(entityId);
            definition.setSitePath(structure.getResourcePath());
            definition.setResourceType(String.valueOf(resource.getResourceType()));
            
            if (editedLocaleEntity != null) {
                definition.getEntities().put(entityId, editedLocaleEntity);
            } else {
                ContentEntityDTO entity = XmlContentParser.documentToEntity(
                    xmlDoc, entityId, "en");
                definition.getEntities().put(entityId, entity);
            }
            
            LOG.info("Successfully loaded definition for entity: {}", entityId);
            return definition;
            
        } catch (Exception e) {
            LOG.error("Error loading definition for entity: {}", entityId, e);
            throw new ContentServiceException("Failed to load definition: " + e.getMessage(), e);
        }
    }
    
    @Transactional(readOnly = true)
    public ContentDefinitionDTO loadNewDefinition(
            String entityId,
            String clientId,
            ContentEntityDTO editedLocaleEntity,
            Collection<String> skipPaths,
            Map<String, String> settingPresets) {
        
        LOG.info("Loading new locale definition for entity: {}", entityId);
        return loadDefinition(entityId, clientId, editedLocaleEntity, skipPaths, settingPresets);
    }
    
    private UUID parseEntityId(String entityId) {
        String uuidPart = entityId.contains("_") ? entityId.substring(0, entityId.indexOf("_")) : entityId;
        return UUID.fromString(uuidPart);
    }
}
