package org.opencms.publishing.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmsPublishDataDTO {
    private CmsPublishOptionsDTO options;
    private List<CmsProjectBeanDTO> projects;
    private CmsPublishGroupListDTO groups;
    private Map<String, CmsWorkflowDTO> workflows;
    private String selectedWorkflowId;
    private String closeLink;
    private boolean showConfirmation;
}
