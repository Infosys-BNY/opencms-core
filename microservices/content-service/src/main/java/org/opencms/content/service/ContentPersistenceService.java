package org.opencms.content.service;

import org.dom4j.Document;
import org.opencms.content.dto.*;
import org.opencms.content.exception.ContentServiceException;
import org.opencms.content.util.XmlContentParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ContentPersistenceService {
    
    private static final Logger LOG = LoggerFactory.getLogger(ContentPersistenceService.class);
    
    @Autowired
    private VfsOperationsServiceImpl vfsService;
    
    @Autowired
    private ContentValidationService validationService;
    
    @Transactional
    @CacheEvict(value = "contentDefinitions", key = "#lastEditedEntity.id")
    public SaveResultDTO saveAndDeleteEntities(
            ContentEntityDTO lastEditedEntity,
            String clientId,
            List<String> deletedEntities,
            Collection<String> skipPaths,
            String lastEditedLocale,
            boolean clearOnSuccess,
            boolean failOnWarnings) {
        
        LOG.info("Saving and deleting entities for client: {}", clientId);
        
        try {
            UUID structureId = parseEntityId(lastEditedEntity.getId());
            
            vfsService.lockResource(structureId);
            
            try {
                ValidationResultDTO validationResult = validationService.validateEntityForSave(
                    lastEditedEntity, clientId, skipPaths);
                
                if (validationResult.hasErrors() || (failOnWarnings && validationResult.hasWarnings())) {
                    LOG.warn("Validation failed for entity: {}", lastEditedEntity.getId());
                    throw new ContentServiceException("Validation failed for entity: " + lastEditedEntity.getId());
                }
                
                byte[] existingContent = vfsService.readFile(structureId);
                Document xmlDoc = XmlContentParser.parseXml(existingContent);
                
                XmlContentParser.updateDocumentFromEntity(xmlDoc, lastEditedEntity, lastEditedLocale);
                
                for (String deleteId : deletedEntities) {
                    String deleteLocale = parseLocale(deleteId);
                    XmlContentParser.removeLocale(xmlDoc, deleteLocale);
                }
                
                byte[] xmlContent = XmlContentParser.serializeXml(xmlDoc);
                vfsService.writeFile(structureId, xmlContent);
                
                LOG.info("Successfully saved entity: {}", lastEditedEntity.getId());
                
                return new SaveResultDTO(false, null, false, null);
                
            } finally {
                if (clearOnSuccess) {
                    vfsService.unlockResource(structureId);
                }
            }
            
        } catch (Exception e) {
            LOG.error("Error saving entities", e);
            throw new ContentServiceException("Failed to save entities: " + e.getMessage(), e);
        }
    }
    
    @Transactional
    @CacheEvict(value = "contentDefinitions", key = "#contentId")
    public String saveValue(String contentId, String contentPath, String locale, String value) {
        LOG.info("Saving value at path: {} for content: {}", contentPath, contentId);
        
        try {
            UUID structureId = UUID.fromString(contentId);
            
            vfsService.lockResource(structureId);
            
            try {
                byte[] contentBytes = vfsService.readFile(structureId);
                Document xmlDoc = XmlContentParser.parseXml(contentBytes);
                
                XmlContentParser.updateValueAtPath(xmlDoc, contentPath, locale, value);
                
                byte[] updatedContent = XmlContentParser.serializeXml(xmlDoc);
                vfsService.writeFile(structureId, updatedContent);
                
                LOG.info("Successfully saved value for content: {}", contentId);
                return "";
                
            } finally {
                vfsService.unlockResource(structureId);
            }
            
        } catch (Exception e) {
            LOG.error("Error saving value", e);
            throw new ContentServiceException("Failed to save value: " + e.getMessage(), e);
        }
    }
    
    private UUID parseEntityId(String entityId) {
        String uuidPart = entityId.contains("_") ? entityId.substring(0, entityId.indexOf("_")) : entityId;
        return UUID.fromString(uuidPart);
    }
    
    private String parseLocale(String entityId) {
        return entityId.contains("_") ? entityId.substring(entityId.indexOf("_") + 1) : "en";
    }
}
