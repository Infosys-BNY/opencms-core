package org.opencms.publishing.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmsWorkflowActionDTO {
    private String action;
    private String label;
    private boolean enabled;
    private boolean isPublish;
}
