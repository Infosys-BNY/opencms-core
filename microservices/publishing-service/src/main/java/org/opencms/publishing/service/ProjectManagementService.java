package org.opencms.publishing.service;

import org.opencms.publishing.dto.CmsProjectBeanDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectManagementService {
    
    public List<CmsProjectBeanDTO> getManageableProjects(Map<String, String> params) {
        List<CmsProjectBeanDTO> projects = new ArrayList<>();
        
        CmsProjectBeanDTO offlineProject = new CmsProjectBeanDTO(
            UUID.randomUUID().toString(),
            "Offline",
            "The Offline Project",
            0,
            0,
            "All Resources"
        );
        
        projects.add(offlineProject);
        return projects;
    }
    
    public String getDefaultProjectId() {
        return UUID.randomUUID().toString();
    }
}
