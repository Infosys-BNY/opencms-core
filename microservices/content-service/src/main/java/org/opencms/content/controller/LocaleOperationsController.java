package org.opencms.content.controller;

import org.opencms.content.dto.ContentEntityDTO;
import org.opencms.content.service.LocaleSynchronizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/content")
public class LocaleOperationsController {
    
    private static final Logger LOG = LoggerFactory.getLogger(LocaleOperationsController.class);
    
    private final LocaleSynchronizationService localeService;
    
    public LocaleOperationsController(LocaleSynchronizationService localeService) {
        this.localeService = localeService;
    }
    
    @PostMapping("/locale/copy")
    public ResponseEntity<Void> copyLocale(
            @RequestBody ContentEntityDTO entityData,
            @RequestParam String sourceLocale,
            @RequestParam String targetLocale,
            @RequestParam(required = false) Collection<String> skipPaths) {
        
        LOG.info("Copying locale from {} to {}", sourceLocale, targetLocale);
        
        localeService.synchronizeLocaleIndependentFields(entityData, skipPaths, sourceLocale, targetLocale);
        
        return ResponseEntity.ok().build();
    }
}
