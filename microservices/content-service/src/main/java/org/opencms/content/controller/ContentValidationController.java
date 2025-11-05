package org.opencms.content.controller;

import org.opencms.content.dto.*;
import org.opencms.content.service.ContentValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/content")
public class ContentValidationController {
    
    private static final Logger LOG = LoggerFactory.getLogger(ContentValidationController.class);
    
    private final ContentValidationService validationService;
    
    public ContentValidationController(ContentValidationService validationService) {
        this.validationService = validationService;
    }
    
    @PostMapping("/validate")
    public ResponseEntity<ValidationResultDTO> validateEntities(
            @RequestBody ContentEntityDTO changedEntity,
            @RequestParam(required = false) Collection<String> changedScopes) {
        
        LOG.info("Validating entities with changed scopes: {}", changedScopes);
        
        ValidationResultDTO result = validationService.validateEntities(changedEntity, changedScopes);
        
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/validate/{entityId}")
    public ResponseEntity<ValidationResultDTO> validateEntity(@PathVariable String entityId) {
        
        LOG.info("Validating entity: {}", entityId);
        
        ValidationResultDTO result = validationService.validateEntity(entityId);
        
        return ResponseEntity.ok(result);
    }
}
