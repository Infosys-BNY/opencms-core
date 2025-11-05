package org.opencms.content.controller;

import org.opencms.content.dto.ContentDefinitionDTO;
import org.opencms.content.dto.ContentEntityDTO;
import org.opencms.content.service.ContentDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/content")
public class ContentDefinitionController {
    
    private static final Logger LOG = LoggerFactory.getLogger(ContentDefinitionController.class);
    
    private final ContentDefinitionService contentDefinitionService;
    
    public ContentDefinitionController(ContentDefinitionService contentDefinitionService) {
        this.contentDefinitionService = contentDefinitionService;
    }
    
    @GetMapping("/{entityId}/initial")
    public ResponseEntity<ContentDefinitionDTO> loadInitialDefinition(
            @PathVariable String entityId,
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) String newLink,
            @RequestParam(required = false) UUID modelFileId,
            @RequestParam(required = false) String editContext,
            @RequestParam(required = false) String mainLocale,
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) String postCreateHandler,
            @RequestParam(required = false) Map<String, String> settingPresets,
            @RequestParam(required = false) String editorStylesheet) {
        
        LOG.info("Loading initial definition for entity: {}", entityId);
        
        ContentDefinitionDTO definition = contentDefinitionService.loadInitialDefinition(
            entityId, clientId, newLink, modelFileId, editContext, mainLocale, 
            mode, postCreateHandler, settingPresets, editorStylesheet);
        
        return ResponseEntity.ok(definition);
    }
    
    @PostMapping("/{entityId}/definition")
    public ResponseEntity<ContentDefinitionDTO> loadDefinition(
            @PathVariable String entityId,
            @RequestParam(required = false) String clientId,
            @RequestBody(required = false) ContentEntityDTO editedLocaleEntity,
            @RequestParam(required = false) Collection<String> skipPaths,
            @RequestParam(required = false) Map<String, String> settingPresets) {
        
        LOG.info("Loading definition for entity: {}", entityId);
        
        ContentDefinitionDTO definition = contentDefinitionService.loadDefinition(
            entityId, clientId, editedLocaleEntity, skipPaths, settingPresets);
        
        return ResponseEntity.ok(definition);
    }
    
    @PostMapping("/{entityId}/locale/new")
    public ResponseEntity<ContentDefinitionDTO> loadNewDefinition(
            @PathVariable String entityId,
            @RequestParam(required = false) String clientId,
            @RequestBody(required = false) ContentEntityDTO editedLocaleEntity,
            @RequestParam(required = false) Collection<String> skipPaths,
            @RequestParam(required = false) Map<String, String> settingPresets) {
        
        LOG.info("Loading new locale definition for entity: {}", entityId);
        
        ContentDefinitionDTO definition = contentDefinitionService.loadNewDefinition(
            entityId, clientId, editedLocaleEntity, skipPaths, settingPresets);
        
        return ResponseEntity.ok(definition);
    }
}
