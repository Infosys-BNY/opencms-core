package org.opencms.publishing.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmsWorkflowResponseDTO {
    private boolean success;
    private String message;
    private List<CmsPublishResourceDTO> resources;
    private List<CmsWorkflowActionDTO> availableActions;
    private String workflowId;
}
