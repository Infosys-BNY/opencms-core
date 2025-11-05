package org.opencms.content.service;

import org.opencms.content.dto.ContentEntityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CategoryService {
    
    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);
    
    public void writeCategories(UUID structureId, ContentEntityDTO entity) {
        LOG.debug("Writing categories for resource: {}", structureId);
    }
}
