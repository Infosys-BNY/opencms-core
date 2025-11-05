package org.opencms.content.controller;

import org.opencms.content.dto.*;
import org.opencms.content.service.ContentPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/content")
public class ContentPersistenceController {
    
    private static final Logger LOG = LoggerFactory.getLogger(ContentPersistenceController.class);
    
    private final ContentPersistenceService persistenceService;
    
    public ContentPersistenceController(ContentPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }
    
    @PostMapping("/save")
    public ResponseEntity<SaveResultDTO> saveAndDeleteEntities(
            @RequestBody ContentEntityDTO lastEditedEntity,
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) List<String> deletedEntities,
            @RequestParam(required = false) Collection<String> skipPaths,
            @RequestParam(required = false) String lastEditedLocale,
            @RequestParam(defaultValue = "false") boolean clearOnSuccess,
            @RequestParam(defaultValue = "false") boolean failOnWarnings) {
        
        LOG.info("Saving and deleting entities, clearOnSuccess: {}", clearOnSuccess);
        
        SaveResultDTO result = persistenceService.saveAndDeleteEntities(
            lastEditedEntity, clientId, deletedEntities, skipPaths, 
            lastEditedLocale, clearOnSuccess, failOnWarnings);
        
        return ResponseEntity.ok(result);
    }
    
    @PutMapping("/{contentId}/value")
    public ResponseEntity<String> saveValue(
            @PathVariable String contentId,
            @RequestParam String contentPath,
            @RequestParam String locale,
            @RequestBody String value) {
        
        LOG.info("Saving value at path: {} for content: {}", contentPath, contentId);
        
        String result = persistenceService.saveValue(contentId, contentPath, locale, value);
        
        return ResponseEntity.ok(result);
    }
}
