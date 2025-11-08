package org.opencms.content.service;

import org.dom4j.Document;
import org.opencms.content.dto.*;
import org.opencms.content.util.XmlContentParser;
import org.opencms.content.util.XmlSchemaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class ContentValidationService {
    
    private static final Logger LOG = LoggerFactory.getLogger(ContentValidationService.class);
    
    @Autowired(required = false)
    private XmlSchemaValidator schemaValidator;
    
    @Autowired
    private VfsOperationsServiceImpl vfsService;
    
    public ValidationResultDTO validateEntities(
            ContentEntityDTO changedEntity,
            Collection<String> changedScopes) {
        
        LOG.debug("Validating entities with changed scopes: {}", changedScopes);
        
        ValidationResultDTO result = new ValidationResultDTO();
        
        if (changedEntity != null) {
            validateEntityInternal(changedEntity, result);
        }
        
        LOG.debug("Validation completed, errors: {}, warnings: {}", 
            result.hasErrors(), result.hasWarnings());
        
        return result;
    }
    
    public ValidationResultDTO validateEntity(String entityId) {
        LOG.debug("Validating entity: {}", entityId);
        
        ValidationResultDTO result = new ValidationResultDTO();
        
        if (entityId == null || entityId.isEmpty()) {
            addError(result, entityId, "id", "Entity ID is required", null);
        }
        
        LOG.debug("Validation completed for entity: {}", entityId);
        return result;
    }
    
    public ValidationResultDTO validateEntityForSave(
            ContentEntityDTO entity,
            String clientId,
            Collection<String> skipPaths) {
        
        LOG.debug("Validating entity for save: {}", entity.getId());
        
        ValidationResultDTO result = new ValidationResultDTO();
        validateEntityInternal(entity, result);
        
        if (schemaValidator != null && !result.hasErrors()) {
            try {
                UUID structureId = parseEntityId(entity.getId());
                byte[] existingContent = vfsService.readFile(structureId);
                Document xmlDoc = XmlContentParser.parseXml(existingContent);
                
                String locale = entity.getId().contains("_") ? entity.getId().substring(entity.getId().indexOf("_") + 1) : "en";
                XmlContentParser.updateDocumentFromEntity(xmlDoc, entity, locale);
                
                String schemaPath = "schemas/" + entity.getTypeName() + ".xsd";
                ValidationResultDTO schemaResult = schemaValidator.validateAgainstSchema(xmlDoc, schemaPath);
                
                result.getErrors().putAll(schemaResult.getErrors());
                result.getWarnings().putAll(schemaResult.getWarnings());
                
            } catch (Exception e) {
                LOG.warn("Schema validation skipped due to error: {}", e.getMessage());
            }
        }
        
        LOG.debug("Validation completed for entity: {}, errors: {}, warnings: {}", 
            entity.getId(), result.hasErrors(), result.hasWarnings());
        
        return result;
    }
    
    private void validateEntityInternal(ContentEntityDTO entity, ValidationResultDTO result) {
        if (entity.getId() == null || entity.getId().isEmpty()) {
            addError(result, entity.getId(), "id", "Entity ID is required", null);
        }
        
        if (entity.getTypeName() == null || entity.getTypeName().isEmpty()) {
            addError(result, entity.getId(), "typeName", "Type name is required", null);
        }
    }
    
    private void addError(ValidationResultDTO result, String entityId, String fieldPath, 
                         String message, String help) {
        if (!result.getErrors().containsKey(entityId)) {
            result.getErrors().put(entityId, new HashMap<>());
        }
        result.getErrors().get(entityId).put(fieldPath, new ValidationMessageDTO(message, help));
    }
    
    private UUID parseEntityId(String entityId) {
        String uuidPart = entityId.contains("_") ? entityId.substring(0, entityId.indexOf("_")) : entityId;
        return UUID.fromString(uuidPart);
    }
}
