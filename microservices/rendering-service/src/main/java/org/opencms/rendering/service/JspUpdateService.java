package org.opencms.rendering.service;

import org.opencms.rendering.entity.TemplateMetadata;
import org.opencms.rendering.model.RenderRequest;
import org.opencms.rendering.repository.TemplateMetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class JspUpdateService {
    
    private static final Logger LOG = LoggerFactory.getLogger(JspUpdateService.class);
    private static final String JSP_EXTENSION = ".jsp";
    
    @Autowired
    private TemplateMetadataRepository metadataRepository;
    
    @Value("${rendering.jsp.repository}")
    private String jspRepository;
    
    public String updateJsp(RenderRequest request) throws IOException {
        String templatePath = request.getTemplatePath();
        boolean online = request.isOnline();
        
        String jspPath = getJspPath(templatePath, online);
        File jspFile = new File(jspPath);
        
        boolean mustUpdate = false;
        
        if (!jspFile.exists()) {
            mustUpdate = true;
            Files.createDirectories(jspFile.getParentFile().toPath());
        } else {
            TemplateMetadata metadata = metadataRepository
                    .findByTemplatePathAndOnline(templatePath, online)
                    .orElse(null);
            
            if (metadata == null || metadata.getLastModified() < System.currentTimeMillis()) {
                mustUpdate = true;
            }
        }
        
        if (mustUpdate) {
            LOG.debug("Updating JSP file: {}", jspPath);
            
            try (FileWriter writer = new FileWriter(jspFile)) {
                writer.write(request.getTemplateContent());
            }
            
            TemplateMetadata metadata = metadataRepository
                    .findByTemplatePathAndOnline(templatePath, online)
                    .orElse(new TemplateMetadata());
            
            metadata.setTemplatePath(templatePath);
            metadata.setFileSystemPath(jspPath);
            metadata.setLastModified(System.currentTimeMillis());
            metadata.setLastCompiled(jspFile.lastModified());
            metadata.setOnline(online);
            
            metadataRepository.save(metadata);
        }
        
        return jspPath;
    }
    
    private String getJspPath(String templatePath, boolean online) {
        String suffix = online ? "/online" : "/offline";
        String path = jspRepository + suffix + templatePath;
        if (!path.endsWith(JSP_EXTENSION)) {
            path += JSP_EXTENSION;
        }
        return path;
    }
}
