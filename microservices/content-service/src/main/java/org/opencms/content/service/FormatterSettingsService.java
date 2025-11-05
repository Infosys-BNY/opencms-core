package org.opencms.content.service;

import org.opencms.content.dto.ContentEntityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FormatterSettingsService {
    
    private static final Logger LOG = LoggerFactory.getLogger(FormatterSettingsService.class);
    
    public boolean saveFormatterSettings(ContentEntityDTO entity, String clientId) {
        LOG.debug("Saving formatter settings for client: {}", clientId);
        return false;
    }
}
