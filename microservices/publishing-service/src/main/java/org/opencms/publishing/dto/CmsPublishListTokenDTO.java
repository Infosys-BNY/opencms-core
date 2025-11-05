package org.opencms.publishing.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmsPublishListTokenDTO {
    private CmsWorkflowDTO workflow;
    private CmsPublishOptionsDTO options;
}
