package org.opencms.content.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ContentCorrectionService {
    
    private static final Logger LOG = LoggerFactory.getLogger(ContentCorrectionService.class);
    
    public boolean checkAutoCorrection(byte[] content) {
        LOG.debug("Checking for auto-correction");
        return false;
    }
}
