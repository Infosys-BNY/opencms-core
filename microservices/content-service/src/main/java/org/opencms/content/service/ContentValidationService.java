package org.opencms.content.service;

import org.opencms.content.dto.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class ContentValidationService {
    
    private static final Logger LOG = LoggerFactory.getLogger(ContentValidationService.class);
    
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
}
