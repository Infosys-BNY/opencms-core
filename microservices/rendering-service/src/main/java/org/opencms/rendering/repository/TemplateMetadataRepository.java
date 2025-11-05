package org.opencms.rendering.repository;

import org.opencms.rendering.entity.TemplateMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemplateMetadataRepository extends JpaRepository<TemplateMetadata, String> {
    
    Optional<TemplateMetadata> findByTemplatePathAndOnline(String templatePath, boolean online);
    
    void deleteByTemplatePath(String templatePath);
}
