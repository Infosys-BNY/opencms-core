package org.opencms.publishing.service;

import org.opencms.publishing.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceGroupingService {
    
    public CmsPublishGroupListDTO getResourceGroups(
        CmsWorkflowDTO workflow,
        CmsPublishOptionsDTO options,
        boolean projectChanged
    ) {
        CmsPublishGroupListDTO groupList = new CmsPublishGroupListDTO();
        
        CmsPublishGroupDTO group = new CmsPublishGroupDTO();
        group.setName("Modified Resources");
        group.setResources(new ArrayList<>());
        group.setAutoSelectable(true);
        
        List<CmsPublishGroupDTO> groups = new ArrayList<>();
        groups.add(group);
        
        groupList.setGroups(groups);
        return groupList;
    }
}
