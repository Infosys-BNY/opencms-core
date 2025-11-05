package org.opencms.content.controller;

import org.opencms.content.dto.ContentEntityDTO;
import org.opencms.content.service.ChangeHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/content")
public class ChangeHandlerController {
    
    private static final Logger LOG = LoggerFactory.getLogger(ChangeHandlerController.class);
    
    private final ChangeHandlerService changeHandlerService;
    
    public ChangeHandlerController(ChangeHandlerService changeHandlerService) {
        this.changeHandlerService = changeHandlerService;
    }
    
    @PostMapping("/change-handlers")
    public ResponseEntity<ContentEntityDTO> callEditorChangeHandlers(
            @RequestBody ContentEntityDTO entityData,
            @RequestParam(required = false) Collection<String> changedScopes) {
        
        LOG.info("Calling editor change handlers for scopes: {}", changedScopes);
        
        ContentEntityDTO result = changeHandlerService.callEditorChangeHandlers(entityData, changedScopes);
        
        return ResponseEntity.ok(result);
    }
}
