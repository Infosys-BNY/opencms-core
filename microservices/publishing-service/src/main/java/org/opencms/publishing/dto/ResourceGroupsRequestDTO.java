package org.opencms.publishing.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceGroupsRequestDTO {
    private CmsWorkflowDTO workflow;
    private CmsPublishOptionsDTO options;
    private Boolean projectChanged;
}
