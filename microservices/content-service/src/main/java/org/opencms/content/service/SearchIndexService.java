package org.opencms.content.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class SearchIndexService {
    
    private static final Logger LOG = LoggerFactory.getLogger(SearchIndexService.class);
    
    public void updateOfflineIndexes(UUID structureId) {
        LOG.debug("Updating offline search indexes for resource: {}", structureId);
    }
}
