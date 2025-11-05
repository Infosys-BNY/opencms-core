package org.opencms.publishing.controller;

import org.opencms.publishing.dto.*;
import org.opencms.publishing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/publishing")
public class PublishingController {
    
    @Autowired
    private WorkflowManagementService workflowService;
    
    @Autowired
    private ProjectManagementService projectService;
    
    @Autowired
    private ResourceGroupingService resourceGroupingService;
    
    @Autowired
    private PublishQueueService publishQueueService;
    
    @Autowired
    private PublishHistoryService publishHistoryService;
    
    @GetMapping("/init")
    public ResponseEntity<CmsPublishDataDTO> getInitData(
        @RequestParam(required = false) Map<String, String> params
    ) {
        if (params == null) {
            params = new HashMap<>();
        }
        
        Map<String, CmsWorkflowDTO> workflows = workflowService.getWorkflows();
        String workflowId = workflowService.getDefaultWorkflowId();
        
        List<CmsProjectBeanDTO> projects = projectService.getManageableProjects(params);
        
        CmsPublishOptionsDTO options = new CmsPublishOptionsDTO();
        options.setIncludeRelated(true);
        options.setIncludeSiblings(false);
        options.setProjectId(projectService.getDefaultProjectId());
        options.setParameters(params);
        
        CmsWorkflowDTO workflow = workflows.get(workflowId);
        CmsPublishGroupListDTO groups = resourceGroupingService.getResourceGroups(
            workflow, options, false
        );
        
        CmsPublishDataDTO data = new CmsPublishDataDTO();
        data.setOptions(options);
        data.setProjects(projects);
        data.setGroups(groups);
        data.setWorkflows(workflows);
        data.setSelectedWorkflowId(workflowId);
        data.setShowConfirmation(false);
        
        return ResponseEntity.ok(data);
    }
    
    @PostMapping("/resource-groups")
    public ResponseEntity<CmsPublishGroupListDTO> getResourceGroups(
        @RequestBody ResourceGroupsRequestDTO request
    ) {
        CmsWorkflowDTO workflow = request.getWorkflow();
        CmsPublishOptionsDTO options = request.getOptions();
        Boolean projectChanged = request.getProjectChanged() != null ? request.getProjectChanged() : false;
        
        CmsPublishGroupListDTO groups = resourceGroupingService.getResourceGroups(
            workflow, options, projectChanged
        );
        
        return ResponseEntity.ok(groups);
    }
    
    @PostMapping("/execute")
    public ResponseEntity<CmsWorkflowResponseDTO> executeAction(
        @RequestBody ExecuteActionRequestDTO request
    ) {
        CmsWorkflowActionDTO action = request.getAction();
        CmsWorkflowActionParamsDTO params = request.getParams();
        
        CmsWorkflowResponseDTO response = workflowService.executePublishAction(action, params);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/options")
    public ResponseEntity<CmsPublishOptionsDTO> getResourceOptions() {
        CmsPublishOptionsDTO options = new CmsPublishOptionsDTO();
        options.setIncludeRelated(true);
        options.setIncludeSiblings(false);
        
        return ResponseEntity.ok(options);
    }
}
