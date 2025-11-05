package org.opencms.content.service;

import org.opencms.content.dto.ContentEntityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ChangeHandlerService {
    
    private static final Logger LOG = LoggerFactory.getLogger(ChangeHandlerService.class);
    
    public ContentEntityDTO callEditorChangeHandlers(
            ContentEntityDTO entityData,
            Collection<String> changedScopes) {
        
        LOG.debug("Calling editor change handlers for scopes: {}", changedScopes);
        return entityData;
    }
}
